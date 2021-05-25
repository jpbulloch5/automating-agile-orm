package testjunk;

import utils.Repository;
import enums.SQLType;
import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;

import java.sql.Connection;
import java.util.UUID;

@Table(tableName = "orm_test")
public class TestEntity extends Repository {
    public TestEntity(Connection conn) {
        super(conn);
    }
    @Column(type = SQLType.UUID, primaryKey = true)
    private UUID orm_test_id;

    @Column(/*columnName = "orm_string", */type = SQLType.VARCHAR)
    private String orm_string;

    @Column(/*columnName = "orm_int", */type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int orm_int;

    @Column(/*columnName = "orm_fk", */type = SQLType.UUID)
    //@ForeignKey(targetTable = "orm_table", targetTableIDName = "test_id")
    private UUID orm_fk;

    public String getOrm_string() {
        return orm_string;
    }

    public int getOrm_int() {
        return orm_int;
    }

    public UUID getOrm_fk() {
        return orm_fk;
    }

    public void setOrm_string(String orm_string) {
        this.orm_string = orm_string;
    }

    public void setOrm_int(int orm_int) {
        this.orm_int = orm_int;
    }

    public void setOrm_fk(UUID orm_fk) {
        this.orm_fk = orm_fk;
    }

    public UUID getOrm_test_id() {
        return orm_test_id;
    }

    public void setOrm_test_id(UUID orm_test_id) {
        this.orm_test_id = orm_test_id;
    }
}
