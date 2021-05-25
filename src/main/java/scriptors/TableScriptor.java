package scriptors;

import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;
import exceptions.MalformedTableException;
import utils.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class TableScriptor {

    public static String buildQueryStatement(Class <? extends Repository> repo) {
        return"SELECT * FROM " + repo.getAnnotation(Table.class).tableName();
    }

    public static String buildCreateTableStatement(Class<? extends Repository> repo) throws MalformedTableException {
        if (!repo.isAnnotationPresent(Table.class)) {
            throw new MalformedTableException("Missing @Table annotation.");
        }


        String tableName = repo.getAnnotation(Table.class).tableName();

        String idName = tableName + "_id";
//        if(repo.getAnnotation(Table.class).idColumnName().equals("")) {
//            idName = tableName + "_id";
//        } else {
//            idName = repo.getAnnotation(Table.class).idColumnName();
//        }

        //iterate through annotated fields
        Field[] fields = repo.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        List<String> foreignKeys = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String columnName;
            String dataType;
            String length = "";
            String constraints = "";
            if(fields[i].isAnnotationPresent(Column.class) && !fields[i].getAnnotation(Column.class).primaryKey()) {//ASSUMING PK FIELD NAME IS EXACTLY TABLE NAME + _id
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
                + tableName + " ("
                + idName + " UUID NOT NULL, ";

        for (String column : columns) {
            createTableStatement += column;
        }

        for (String snippet : foreignKeys) {
            createTableStatement += snippet;
        }


        createTableStatement += "PRIMARY KEY(" + idName + "))";

        return createTableStatement;
    }
}
