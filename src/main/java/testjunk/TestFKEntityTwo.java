package testjunk;

import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;
import enums.SQLType;
import utils.Repository;

import java.util.UUID;
@Table(tableName = "orm_test_fk_two")
public class TestFKEntityTwo extends Repository{
    @Column(columnName = "orm_string", type = SQLType.VARCHAR)
    private String str;

    @Column(columnName = "orm_int", type = SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int i;

    @Column(columnName = "orm_fk", type = SQLType.UUID)
    @ForeignKey(referencedTable = "orm_test_fk", referencedTableID = "fk_id")
    private UUID fk_id;
}
