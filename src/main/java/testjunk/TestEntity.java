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

    @Column(columnName = "orm_string", type = SQLType.VARCHAR)
    private String str;

    @Column(columnName = "orm_int", type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int i;

    @Column(columnName = "orm_fk", type = SQLType.UUID)
    //@ForeignKey(targetTable = "orm_table", targetTableIDName = "test_id")
    private UUID fk_id;

}
