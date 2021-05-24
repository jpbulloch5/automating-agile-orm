package utils;

import annotations.Column;
import scriptors.TableScriptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Repository {
    private Connection conn;
    public Repository(Connection conn) {
        this.conn = conn;
    }

    public void save() {
        //create or update
        //reflect on this (child object) fields
        //build a sql statement that does insert into, or update
        //execute it

    }

    public void refresh() {
        //re-load from db based on UUID
        //reflect on this (child object) fields
        //build a sql query to query those fields
        //run it
        //update fields
    }

    public void delete() {
        //remove from db
        //delete where UUID = UUID;
    }

    public static List<Repository> query(Connection conn, Class<? extends Repository> repo)
            throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String sql = TableScriptor.buildQueryStatement(repo);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Repository> results = new LinkedList<>();
        while(rs.next())
        {
            Repository newRepo = repo.newInstance(conn);

            Field[] fields = repo.getDeclaredFields();
            for (Field field : fields) {
                field.set(newRepo, rs.getObject(field.getAnnotation(Column.class).columnName()));
            }
            results.add(newRepo);
        }






        return new ArrayList<Repository>();

    }
}
