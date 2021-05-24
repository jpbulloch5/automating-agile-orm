package testjunk;

import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;
import enums.SQLType;
import utils.Repository;

import java.sql.Connection;
import java.util.UUID;
@Table(tableName = "orm_test_fk_two")
public class TestFKEntityTwo extends Repository{
    public TestFKEntityTwo(Connection conn) {
        super(conn);
    }

    @Column(/*columnName = "orm_string", */type = SQLType.VARCHAR)
    private String test_string_two;

    @Column(/*columnName = "orm_int", */type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int test_int_two;

    @Column(/*columnName = "orm_fk", */type = SQLType.UUID)
    @ForeignKey(referencedTable = "orm_test_fk")
    private UUID test_fk_id_two;


}
