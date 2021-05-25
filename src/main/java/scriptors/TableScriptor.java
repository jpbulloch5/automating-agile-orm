package scriptors;

import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;
import exceptions.MalformedTableException;
import utils.Repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class TableScriptor {

    public static String buildQueryStatement(Class <? extends Repository> repo) {
        return"SELECT * FROM " + repo.getAnnotation(Table.class).tableName();
    }

    public static PreparedStatement buildSaveStatement(Repository repo) throws IllegalAccessException, SQLException {
        Field[] fields = repo.getClass().getDeclaredFields();
        List<Object> data = new LinkedList<Object>();
        String tableName = repo.getClass().getAnnotation(Table.class).tableName();
        String updateSQL = "UPDATE " + tableName + " SET ";
        String insertSQL = "INSERT INTO " + tableName + " (";
        String insertSQLValues = " VALUES (";
        String selectSQL = "SELECT * FROM " + tableName + " WHERE " + tableName + "_id = ?";
        String primaryKey = "";


        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            //System.out.println("DEBUG: setting field " + i + " to accessible.");
            if(fields[i].isAnnotationPresent(Column.class)) {
                if (fields[i].getAnnotation(Column.class).primaryKey()) {
                    primaryKey = ((UUID)fields[i].get(repo)).toString();
                } else {
                    updateSQL += fields[i].getName() + " = ?";
                    insertSQL += fields[i].getName();
                    insertSQLValues += "?";
                    data.add(fields[i].get(repo));
                    if(i == fields.length - 1) {
                        updateSQL += " ";
                        insertSQL += ", " + tableName + "_id)";
                        insertSQLValues += ", ?)";
                    }
                    else {
                        updateSQL += ", ";
                        insertSQL += ", ";
                        insertSQLValues += ", ";
                    }
                }
            }
            //System.out.println("DEBUG: setting field " + i + " to inaccessible.");
            fields[i].setAccessible(false);
        }

        updateSQL += "WHERE " + tableName + "_id = ?";
        data.add(primaryKey);



        PreparedStatement pstmt = repo.getConn().prepareStatement(selectSQL);
        pstmt.setObject(1, UUID.fromString(primaryKey));
        System.out.println(pstmt.toString());
        ResultSet exists = pstmt.executeQuery();

        if(exists.next()) {
            //entry exists, UPDATE
            System.out.println(updateSQL);
            pstmt = repo.getConn().prepareStatement(updateSQL);
        } else {
            //entry doesn't exist, INSERT
            System.out.println(insertSQL + insertSQLValues);
            pstmt = repo.getConn().prepareStatement(insertSQL + insertSQLValues);
        }


        for (int i = 0; i < data.size(); i++) {
            pstmt.setObject(i+1, data.get(i));
        }

        return pstmt;
    }

    public static PreparedStatement buildDeleteStatement(Repository repo) throws IllegalAccessException, SQLException {
        String tableName = repo.getClass().getAnnotation(Table.class).tableName();
        String sql = "DELETE FROM " + tableName + " WHERE " + tableName + "_id = ?";
        Field[] fields = repo.getClass().getDeclaredFields();
        String primaryKey = "";
        for (Field field : fields) {
            //System.out.println("field: " + field.getName());
            if(field.getAnnotation(Column.class).primaryKey()) {
                field.setAccessible(true);
                //System.out.println("Primary key found!");
                primaryKey = field.get(repo).toString();
                field.setAccessible(false);
            }
        }
        PreparedStatement pstmt = repo.getConn().prepareStatement(sql);
        pstmt.setObject(1, primaryKey);

        return pstmt;

    }

    public static PreparedStatement buildRefreshStatement(Repository repo) throws IllegalAccessException, SQLException {
        String tableName = repo.getClass().getAnnotation(Table.class).tableName();
        String sql = "SELECT * FROM " + tableName + " WHERE " + tableName + "_id = ?";
        String primaryKey = "";
        Field[] fields = repo.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getAnnotation(Column.class).primaryKey()) {
                field.setAccessible(true);
                primaryKey = field.get(repo).toString();
                field.setAccessible(false);
            }
        }
        PreparedStatement pstmt = repo.getConn().prepareStatement(sql);
        pstmt.setObject(1, primaryKey);

        return pstmt;
    }


    public static String buildCreateTableStatement(Class<? extends Repository> repo) throws MalformedTableException {
        if (!repo.isAnnotationPresent(Table.class)) {
            throw new MalformedTableException("Missing @Table annotation.");
        }


        String tableName = repo.getAnnotation(Table.class).tableName();

        String idName = tableName + "_id";

        //iterate through annotated fields
        Field[] fields = repo.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        List<String> foreignKeys = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String columnName;
            String dataType;
            String length = "";
            String constraints = "";
            if(fields[i].isAnnotationPresent(Column.class)/* && !fields[i].getAnnotation(Column.class).primaryKey()*/) {//ASSUMING PK FIELD NAME IS EXACTLY TABLE NAME + _id
                columnName = fields[i].getName();
                dataType = " " + fields[i].getAnnotation(Column.class).type().toString();


                //Set VARCHAR Length
                if (dataType.equals(" VARCHAR") && fields[i].getAnnotation(Column.class).length() != -1) {
                    length = "(" + fields[i].getAnnotation(Column.class).length() + ")";
                }

                //Set NOT NULL Constraint
                if(fields[i].getAnnotation(Column.class).nonNull()) {
                    constraints = " NOT NULL";
                } /*else if (fields[i].isAnnotationPresent(DefaultValue.class)) {
                    throw new MalformedTableException("Table cannot be nullable without a default value.")
                }*/ //Why does nullable require default again? Does it? Leaving this out for now.

            } else {
                throw new MalformedTableException("Field " + fields[i].getName() + " missing @Column annotation.");
            }


            if(fields[i].isAnnotationPresent(DefaultValue.class)) {
                constraints += " DEFAULT " + fields[i].getAnnotation(DefaultValue.class).defaultValue();
            }

            String assembledcolumnSnippet =
                    columnName
                    + dataType
                    + length
                    + constraints
                    +", ";

            columns.add(assembledcolumnSnippet);


            //Foreign Keys
            if(fields[i].isAnnotationPresent(ForeignKey.class)) {
                String referencedTable = fields[i].getAnnotation(ForeignKey.class).referencedTable();
                String referencedColumnName = fields[i].getAnnotation(ForeignKey.class).referencedTable() + "_id";

                String assembledForeignKeySnippet = "FOREIGN KEY ("
                        + columnName + ")"
                        + " REFERENCES "
                        + referencedTable + " ("
                        + referencedColumnName + "), ";

                foreignKeys.add(assembledForeignKeySnippet);

            }
        }

        //Assemble the SQL statement
        String createTableStatement = "CREATE TABLE "
                + tableName + " (";
                //+ idName + " UUID NOT NULL, ";

        for (String column : columns) {
            createTableStatement += column;
        }

        for (String snippet : foreignKeys) {
            createTableStatement += snippet;
        }


        createTableStatement += "PRIMARY KEY(" + idName + "))";

        //System.out.println("DEBUG: " + createTableStatement);

        return createTableStatement;
    }
}
