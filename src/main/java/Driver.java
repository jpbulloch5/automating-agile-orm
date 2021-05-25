
import exceptions.DBConnectionException;
import testjunk.TestFKEntity;
import testjunk.TestFKEntityTwo;
import utils.ConnectionFactory;
import utils.Initializer;
import testjunk.TestEntity;
import utils.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

/*
host=database.ciwpi6yisnng.us-east-2.rds.amazonaws.com
port=5432
dbname=postgres
schemaname=test_schema
username=appuser
password=4ppp4ssDontGoChasingWaterfalls
driver=org.postgresql.Driver
 */

public class Driver {
    public static void main(String[] args){
        Connection conn;
        try {
            conn = connect("src/main/resources/jdbc.properties");
            //conn = connect("database.ciwpi6yisnng.us-east-2.rds.amazonaws.com",5432,"postgres","test_schema","appuser","4ppp4ssDontGoChasingWaterfalls","org.postgresql.Driver");
            TreeMap<String, Class<? extends Repository>> tableList = new TreeMap<>();
            tableList.put("orm_test_fk_two", TestFKEntityTwo.class);
            tableList.put("orm_test_fk", TestFKEntity.class);
            tableList.put("orm_test", TestEntity.class);

            //create tables
        //    Initializer.initializeTableList(tableList, conn);


         List<Repository> queryResults = Repository.query(conn, TestEntity.class);


            for (Repository queryResult : queryResults) {
                System.out.println("Result: " + queryResult.toString());
            }


        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

    }

    public static Connection connect(String host, int port, String db, String schema,
                                     String user, String pass, String driver) throws DBConnectionException {
        Connection conn = ConnectionFactory.getConnection(host, port, db, schema, user, pass, driver);
        return conn;
    }

    public static Connection connect(String connPropsFilePath) throws DBConnectionException, IOException {
        Properties props = new Properties();
        FileReader jdbcPropFile = new FileReader(connPropsFilePath);
        props.load(jdbcPropFile);
        return Driver.connect(props.getProperty("host"),
                Integer.parseInt(props.getProperty("port")),
                props.getProperty("dbname"),
                props.getProperty("schemaname"),
                props.getProperty("username"),
                props.getProperty("password"),
                props.getProperty("driver")
        );
    }
}
