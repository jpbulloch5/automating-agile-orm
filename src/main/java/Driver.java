
import exceptions.DBConnectionException;
import testjunk.DBTable;
import utils.ConnectionFactory;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;


import java.util.Properties;



public class Driver {
    public static void main(String[] args){
        Connection conn;
        try {
            conn = connect("src/main/resources/jdbc.properties");

            DBTable newTable = new DBTable(conn);
            newTable.InitializeTable();
            //Initializer.initializeTable(DBTable.class, conn);

//            TestEntity newEntity = new TestEntity(conn);
//            newEntity.setOrm_test_id(UUID.randomUUID());
//            newEntity.setOrm_string("Brand New Entity");
//            newEntity.setOrm_int(10);
//            newEntity.setOrm_fk(UUID.randomUUID());
//
//            TestEntity updateEntity = new TestEntity(conn);
//            updateEntity.setOrm_test_id(UUID.fromString("76420919-ab02-4e1f-99b5-8726474986a9"));
//            updateEntity.setOrm_string("Updated Entity");
//            updateEntity.setOrm_int(10);
//            updateEntity.setOrm_fk(UUID.fromString("2c8f28fe-9958-4c6b-9018-294b743c447c"));
//
//            //System.out.println("ID: " + newEntity.getOrm_test_id());
//
//            newEntity.save();
//            updateEntity.save();
//
//            TestEntity refreshEntity = new TestEntity(conn);
//            refreshEntity.setOrm_test_id(UUID.fromString("76420919-ab02-4e1f-99b5-8726474986a9"));
//
//            refreshEntity.refresh();
//            System.out.println("Refresh: " + refreshEntity.getOrm_string());
//
//            TestEntity deleteEntity = new TestEntity(conn);
//            deleteEntity.setOrm_test_id(UUID.fromString("4c83a880-bc26-11eb-855b-07091210f320"));
//            deleteEntity.delete();



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
