package testjunk;

import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;
import enums.SQLType;
import utils.Repository;

import java.sql.Connection;
import java.util.UUID;
@Table(tableName = "orm_test_fk", idColumnName = "fk_id")
public class TestFKEntity extends Repository{
    public TestFKEntity(Connection conn) {
        super(conn);
    }

    @Column(columnName = "orm_string", type = SQLType.VARCHAR)
    private String str;

    @Column(columnName = "orm_int", type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int i;

    @Column(columnName = "orm_fk", type = SQLType.UUID)
    @ForeignKey(referencedTable = "orm_test")
    private UUID fk_id;

}
