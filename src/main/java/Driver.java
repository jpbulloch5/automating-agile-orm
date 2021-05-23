import Utils.ConnectionFactory;
import Utils.TableInitializer;
import exceptions.DBConnectionException;
import testjunk.TestEntity;

import java.sql.Connection;

public class Driver {
    public static void main(String[] args){

        try{
            Connection conn = ConnectionFactory.getConnection();
        } catch (DBConnectionException e) {
            System.out.println("DBConnectionException: " + e);
        }


        TableInitializer.InitializeTable(TestEntity.class);
    }
}
