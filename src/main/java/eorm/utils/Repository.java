package eorm.utils;

import eorm.exceptions.MalformedTableException;
import eorm.scriptors.SQLScriptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class Repository {
    private Connection conn;
    //private Repository newRepo;

    public Repository(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void initializeTable() throws SQLException, MalformedTableException {
        TableInitializer.initializeTable(this);
    }

    public void save() throws IllegalAccessException, SQLException {
        PreparedStatement pstmt = SQLScriptor.buildSaveStatement(this);
        pstmt.executeUpdate();
    }

    public void refresh() throws SQLException, IllegalAccessException {
        PreparedStatement pstmt = SQLScriptor.buildRefreshStatement(this);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(this, rs.getObject(field.getName()));
                field.setAccessible(false);
            }
        }

    }

    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement pstmt = SQLScriptor.buildDeleteStatement(this);
        pstmt.executeUpdate();
    }

    public static List<Repository> query(Connection conn, Class<? extends Repository> repo)
            throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String sql = SQLScriptor.buildQueryStatement(repo);
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
            }
            results.add(newRepo);
        }
        return results;
    }
}
