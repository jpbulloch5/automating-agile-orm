
import exceptions.DBConnectionException;
import scriptors.TableScriptor;
import testjunk.TestFKEntity;
import testjunk.TestFKEntityTwo;
import utils.ConnectionFactory;
import testjunk.TestEntity;
import utils.Initializer;
import utils.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;


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
            //Initializer.initializeTableList(tableList, conn);


            List<Repository> queryResults = Repository.query(conn, TestEntity.class);


            for (Repository queryResult : queryResults) {
                //System.out.println("Result: " + queryResult.toString());
                System.out.println("================================================");
                System.out.println("ID: " + ((TestEntity)queryResult).getOrm_test_id());
                System.out.println("orm_string: " + ((TestEntity)queryResult).getOrm_string());
                System.out.println("orm_int: " + ((TestEntity)queryResult).getOrm_int());
                System.out.println("orm_fk: " + ((TestEntity)queryResult).getOrm_fk());
                System.out.println("================================================");
            }

            //TableScriptor.buildSaveStatement(queryResults.get(0));
            TestEntity updateEntity = new TestEntity(conn);
            updateEntity.setOrm_test_id(UUID.fromString("57785682-bc26-11eb-855c-b7b6b5f5da34"));
            updateEntity.setOrm_string("existingEntity");
            updateEntity.setOrm_int(1);
            updateEntity.setOrm_fk(UUID.fromString("5d429f5a-bc26-11eb-855d-374dc522fb11"));

            TestEntity newEntity = new TestEntity(conn);
            newEntity.setOrm_test_id(UUID.randomUUID());
            newEntity.setOrm_string("newTestString");
            newEntity.setOrm_int(0);
            newEntity.setOrm_fk(UUID.randomUUID());

            System.out.println("update existing entry...");
            updateEntity.save();

            System.out.println("create new entry...");
            newEntity.save();



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
