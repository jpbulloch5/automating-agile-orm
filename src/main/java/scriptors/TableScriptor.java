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

public class TableScriptor {
    public static String buildCreateTableStatement(Class<? extends Repository> repo) throws MalformedTableException {
        if (!repo.isAnnotationPresent(Table.class)) {
            throw new MalformedTableException("Missing @Table annotation.");
        }
        String tableName = repo.getAnnotation(Table.class).tableName();

        String idName;
        if(repo.getAnnotation(Table.class).idColumnName().equals("")) {
            idName = tableName + "_id";
        } else {
            idName = repo.getAnnotation(Table.class).idColumnName();
        }

        //iterate through annotated fields
        Field[] fields = repo.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        List<String> foreignKeys = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            String columnName;
            String dataType;
            String length = "";
            String constraints = "";
            if(fields[i].isAnnotationPresent(Column.class)) {
                columnName = fields[i].getAnnotation(Column.class).columnName();
                dataType = " " + fields[i].getAnnotation(Column.class).type().toString();
                if (dataType.equals(" VARCHAR") && fields[i].getAnnotation(Column.class).length() != -1) {
                    length = "(" + fields[i].getAnnotation(Column.class).length() + ")";
                }

                if(fields[i].getAnnotation(Column.class).nonNull()) {
                    constraints = " NOT NULL";
                }

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
                String referencedColumnName = "";
                if(fields[i].getAnnotation(ForeignKey.class).referencedTableID().equals("")) {
                    referencedColumnName = fields[i].getAnnotation(ForeignKey.class).referencedTable() + "_id";
                } else {
                    referencedColumnName = fields[i].getAnnotation(ForeignKey.class).referencedTableID();
                }

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
