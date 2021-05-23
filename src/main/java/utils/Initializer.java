package utils;

import annotations.ForeignKey;
import annotations.Table;
import exceptions.MalformedTableException;
import scriptors.TableScriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Initializer {

    //SORT TABLES TO CREATE
    //use a structure that can be re-ordered rapidly... treemap? K=tableName V=Class obj
    //while tree not empty, get first key -> get table
    //check if table references another
    //      if yes - call recusrive reference sorting function
    //                  recursively look for FK references repeating this step until
    //                  if FK -> PK not found, check to see if it's already been moved.
    //                          found table with no references, pop into end of sorted list
    //      if no - pop table into end of sorted list
    //once tree is empty, the sorted list should be in "waterfall" order.



    public static void initializeTable(Class<? extends Repository> repo, Connection conn) throws MalformedTableException, SQLException {
        String sql = TableScriptor.buildCreateTableStatement(repo);
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
