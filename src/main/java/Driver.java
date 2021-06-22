
import eorm.exceptions.DBConnectionException;
import eorm.utils.Repository;
import eorm.utils.TableInitializer;
import testjunk.DBTable;
import eorm.utils.ConnectionFactory;
import testjunk.TestEntity;
import testjunk.TestFKEntity;
import testjunk.TestFKEntityTwo;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;


import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;


public class Driver {
    public static void main(String[] args){
        Connection conn;
        try {
            conn = connect("src/main/resources/jdbc.properties");


            TreeMap<String, Repository> initializerTest = new TreeMap<>();

            DBTable table1 = new DBTable(conn);
            initializerTest.put("DBTable", table1);
            TestFKEntity table3 = new TestFKEntity(conn);
            initializerTest.put("orm_test_fk", table3);
            TestEntity table2 = new TestEntity(conn);
            initializerTest.put("orm_test", table2);
            TestFKEntityTwo table4 = new TestFKEntityTwo(conn);
            initializerTest.put("orm_test_fk_two", table4);

            TableInitializer.initializeTableList(initializerTest, conn);





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
