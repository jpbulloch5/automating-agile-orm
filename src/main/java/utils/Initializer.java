package utils;

import annotations.ForeignKey;
import annotations.Table;
import exceptions.MalformedTableException;
import scriptors.SQLScriptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Initializer {
    public static void initializeTable(Class<? extends Repository> repo, Connection conn) throws MalformedTableException, SQLException {
        String sql = SQLScriptor.buildCreateTableStatement(repo);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public static void initializeTableList(TreeMap<String, Class<? extends Repository>> tables, Connection conn) throws MalformedTableException, SQLException {
        while(!tables.isEmpty()) {
            Class<? extends Repository> table = tables.firstEntry().getValue();
            recursiveDependencySearch(conn, tables, table);
        }
    }

    private static void recursiveDependencySearch(Connection conn, TreeMap<String,
            Class<? extends Repository>> tables, Class<? extends Repository> table) throws SQLException, MalformedTableException {
        Field[] fields = table.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(ForeignKey.class)) {
                Class<? extends Repository> refTable = tables.get(field.getAnnotation(ForeignKey.class).referencedTable());
                if(refTable != null) {
                    recursiveDependencySearch(conn, tables, refTable);
                }
            }
        }

        initializeTable(table, conn);
        tables.remove(table.getAnnotation(Table.class).tableName());
    }
}
