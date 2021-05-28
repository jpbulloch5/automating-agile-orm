package testjunk;

import eorm.annotations.Column;
import eorm.annotations.DefaultValue;
import eorm.annotations.ForeignKey;
import eorm.annotations.Table;
import eorm.enums.SQLType;
import eorm.utils.Repository;

import java.sql.Connection;
import java.util.UUID;
@Table(tableName = "orm_test_fk")
public class TestFKEntity extends Repository{
    public TestFKEntity(Connection conn) {
        super(conn);
    }
    @Column(type = SQLType.UUID, primaryKey = true)
    private UUID orm_test_fk_id;

    @Column(/*columnName = "orm_string", */type = SQLType.VARCHAR)
    private String test_string;

    @Column(/*columnName = "orm_int", */type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int test_int;

    @Column(/*columnName = "orm_fk", */type = SQLType.UUID)
    @ForeignKey(referencedTable = "orm_test")
    private UUID test_fk_id;

    public String getTest_string() {
        return test_string;
    }

    public int getTest_int() {
        return test_int;
    }

    public UUID getTest_fk_id() {
        return test_fk_id;
    }

    public void setTest_string(String test_string) {
        this.test_string = test_string;
    }

    public void setTest_int(int test_int) {
        this.test_int = test_int;
    }

    public void setTest_fk_id(UUID test_fk_id) {
        this.test_fk_id = test_fk_id;
    }

    public UUID getOrm_test_fk_id() {
        return orm_test_fk_id;
    }

    public void setOrm_test_fk_id(UUID orm_test_fk_id) {
        this.orm_test_fk_id = orm_test_fk_id;
    }
}
