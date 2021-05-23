package Utils;

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
    private static String connPropsFilePath;

    static {
        connPropsFilePath = "src/main/resources/jdbc.properties";
    }

    /**
     * Private constructor. Attempts to establish a connection with database based on parameters
     * found in properties file. If an exception is thrown during this process, application
     * quit() method is invoked. If successful connection object is stored and served
     * by getConnection() method.
     */
    private ConnectionFactory() throws DBConnectionException{
        Properties props = new Properties();
        try (FileReader jdbcPropFile = new FileReader(connPropsFilePath)){
            props.load(jdbcPropFile);
            Class.forName(props.getProperty("driver"));
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://"
                            + props.getProperty("host") + ":"
                            + props.getProperty("port") + "/"
                            + props.getProperty("dbname")
                            + "?currentSchema=" + props.getProperty("schemaname"),
                    props.getProperty("username"),
                    props.getProperty("password"));
            if (connection == null) {
                throw new DBConnectionException("Unable to connect to database.");
            } else { System.out.println("DEBUG: connection established."); }
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
    public static Connection getConnection() throws DBConnectionException {
        if(connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
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
