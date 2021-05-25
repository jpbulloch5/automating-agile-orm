package utils;

import annotations.Column;
import scriptors.TableScriptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Repository {
    private Connection conn;
    private Repository newRepo;

    public Repository(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void save() throws IllegalAccessException, SQLException {
        PreparedStatement pstmt = TableScriptor.buildSaveStatement(this);
        PreparedStatement fixedStmt = conn.prepareStatement(pstmt.toString());//this is ugly, why doesn't it like UUID's without this?
        System.out.println("pstmt toString(): " + pstmt.toString());

        //pstmt.executeUpdate();
        fixedStmt.executeUpdate();
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
            Repository newRepo = repo.getConstructor(Connection.class).newInstance(conn);

            Field[] fields = repo.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible (true);
                field.set(newRepo, rs.getObject(field.getName()));
                field.setAccessible (false);

                //delete this later
                //System.out.println (field);

            }


            results.add(newRepo);
        }
        return results;
    }
}
