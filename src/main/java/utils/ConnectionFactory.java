package utils;

import exceptions.DBConnectionException;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * This factory maintains connection to the database, and returns connection objects as needed.
 *
 */
public class ConnectionFactory {
    private static ConnectionFactory connectionFactory;
    private static Connection connection;

    /**
     * Private constructor. Attempts to establish a connection with database based on parameters
     * found in properties file. If an exception is thrown during this process, application
     * quit() method is invoked. If successful connection object is stored and served
     * by getConnection() method.
     */


    private ConnectionFactory(String host, int port, String db, String schema, String user, String pass, String driver) throws DBConnectionException {
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://"
                            + host + ":"
                            + port + "/"
                            + db
                            + "?currentSchema=" + schema,
                    user,
                    pass);
            if (connection == null) {
                throw new DBConnectionException("Unable to connect to database.");
            } //else { System.out.println("DEBUG: connection established."); }
            ConnectionFactory.connection = connection;

        } catch (Exception e) {
            throw new DBConnectionException("Exception thrown when connecting to database: " + e);
        }
    }


    /**
     * Returns connection reference used to access database. If connection not yet
     * established, invokes private constructor.
     * @return reference to connection object used to access database
     */


    public static Connection getConnection(String host, int port, String db, String schema, String user, String pass, String driver) throws DBConnectionException {
        if(connectionFactory == null) {
            connectionFactory = new ConnectionFactory(host, port, db, schema, user, pass, driver);
        }
        return connection;
    }

    /**
     * Method to force connection to close. Gracefully terminates connection with database.
     * Can be used to cause next call to getConnection() to invoke constructor
     * and establish a new connection to database.
     */
    public void closeConnection() throws SQLException {
        connection.close();
        connectionFactory = null;

    }

    /**
     * calls closeConnection() upon destruction of this object to gracefully terminate
     * connection before garbage collection
     */
    public void finalize() throws SQLException{
        this.closeConnection();
    }

}
