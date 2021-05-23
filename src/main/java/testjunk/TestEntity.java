package testjunk;

import Utils.Repository;
import annotations.Column;
import annotations.DefaultValue;
import annotations.ForeignKey;
import annotations.Table;

import java.util.UUID;

@Table(tableName = "orm_table")
public class TestEntity extends Repository {
    @Column(columnName = "orm_string", type = Column.SQLType.VARCHAR)
    private String str;

    @Column(columnName = "orm_int", type = Column.SQLType.INT, nonNull = true)
    @DefaultValue(defaultValue = "1")
    private int i;

    @Column(columnName = "orm_fk", type = Column.SQLType.UUID)
    @ForeignKey(targetTable = "orm_fk_table")
    private UUID fk_id;

}
