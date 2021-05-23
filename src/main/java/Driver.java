import scriptors.TableScriptor;
import testjunk.TestFKEntity;
import testjunk.TestFKEntityTwo;
import utils.ConnectionFactory;
import utils.Initializer;
import exceptions.DBConnectionException;
import testjunk.TestEntity;
import utils.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Driver {

    public static void main(String[] args){
        Connection conn;
        try {
            conn = ConnectionFactory.getConnection();
            TreeMap<String, Class<? extends Repository>> tableList = new TreeMap<>();
            tableList.put("orm_test_fk", TestFKEntity.class);
            tableList.put("orm_test", TestEntity.class);
            tableList.put("orm_test_fk_two", TestFKEntityTwo.class);

            Initializer.initializeTableList(tableList, conn);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }



    }
}
