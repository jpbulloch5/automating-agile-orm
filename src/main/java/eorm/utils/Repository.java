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

/**
 * The repository class is meant to be inherited by entities. This class has CRUD functions that
 * can then be invoked by those entities. An entity should have it's own private fields, public getters and setters,
 * a public constructor that takes in a connection and passes it to super(), and may require special care for jackson
 * to marshall/unmarshall it.
 */
public abstract class Repository {
    private Connection conn;

    public Repository(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * Executes table creation SQL based on entity reflection.
     * @throws SQLException
     * @throws MalformedTableException
     */
    public void initializeTable() throws SQLException, MalformedTableException {
        TableInitializer.initializeTable(this);
    }

    /**
     * executes row creation/update SQL based on entity reflection.
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public void save() throws IllegalAccessException, SQLException {
        PreparedStatement pstmt = SQLScriptor.buildSaveStatement(this);
        pstmt.executeUpdate();
    }

    /**
     * Executes query based on entity reflection. Requires a UUID. Populates all other fields
     * from the corresponding table row.
     * @throws SQLException
     * @throws IllegalAccessException
     */
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

    /**
     * Executes SQL to delete the corresponding row from the table.
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement pstmt = SQLScriptor.buildDeleteStatement(this);
        pstmt.executeUpdate();
    }

    /**
     * This static function queries the entire table, and is not specific to a particular entity. Use this to retrieve
     * data without knowing the UUID, and filter.
     * @param conn - connection object linked to the datasource
     * @param repo - entity class associated with the table to query.
     * @return - List of entity objects corresponding to the entity parameter.
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
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
