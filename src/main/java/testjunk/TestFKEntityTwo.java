package testjunk;

import eorm.annotations.Column;
import eorm.annotations.DefaultValue;
import eorm.annotations.ForeignKey;
import eorm.annotations.Table;
import eorm.enums.SQLType;
import eorm.utils.Repository;

import java.sql.Connection;
import java.util.UUID;

@Table(tableName = "orm_test_fk_two")
public class TestFKEntityTwo extends Repository{
    public TestFKEntityTwo(Connection conn) {
        super(conn);
    }

    @Column(type = SQLType.UUID, primaryKey = true)
    private UUID orm_test_fk_two_id;

    @Column(/*columnName = "orm_string", */type = SQLType.VARCHAR)
    private String test_string_two;

    @Column(/*columnName = "orm_int", */type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int test_int_two;

    @Column(/*columnName = "orm_fk", */type = SQLType.UUID)
    @ForeignKey(referencedTable = "orm_test_fk")
    private UUID test_fk_id_two;

    public String getTest_string_two() {
        return test_string_two;
    }

    public int getTest_int_two() {
        return test_int_two;
    }

    public UUID getTest_fk_id_two() {
        return test_fk_id_two;
    }

    public void setTest_string_two(String test_string_two) {
        this.test_string_two = test_string_two;
    }

    public void setTest_int_two(int test_int_two) {
        this.test_int_two = test_int_two;
    }

    public void setTest_fk_id_two(UUID test_fk_id_two) {
        this.test_fk_id_two = test_fk_id_two;
    }

    public UUID getOrm_test_fk_two_id() {
        return orm_test_fk_two_id;
    }

    public void setOrm_test_fk_two_id(UUID orm_test_fk_two_id) {
        this.orm_test_fk_two_id = orm_test_fk_two_id;
    }
}
