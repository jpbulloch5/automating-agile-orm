package eorm.utils;

import eorm.annotations.ForeignKey;
import eorm.annotations.Table;
import eorm.exceptions.MalformedTableException;
import eorm.scriptors.SQLScriptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TableInitializer {
    public static void initializeTable(Repository repo) throws MalformedTableException, SQLException {
        String sql = SQLScriptor.buildCreateTableStatement(repo);
        Connection conn = repo.getConn();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    public static void initializeTableList(TreeMap<String, Repository> tables, Connection conn) throws MalformedTableException, SQLException {
        while(!tables.isEmpty()) {
            Repository table = tables.firstEntry().getValue();
            recursiveDependencySearch(tables, table);
        }
    }

    private static void recursiveDependencySearch(TreeMap<String, Repository> tables, Repository table) throws SQLException, MalformedTableException {
        Field[] fields = table.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(ForeignKey.class)) {
                Repository refTable = tables.get(field.getAnnotation(ForeignKey.class).referencedTable());
                if(refTable != null) {
                    recursiveDependencySearch(tables, refTable);
                }
            }
        }

        initializeTable(table);
        tables.remove(table.getClass().getAnnotation(Table.class).tableName());
    }
}
