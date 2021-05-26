package testjunk;

import annotations.Column;
import annotations.DefaultValue;
import annotations.Table;
import enums.SQLType;
import utils.Repository;

import java.sql.Connection;
import java.util.UUID;

@Table(tableName = "DBTable")
public class DBTable extends Repository {
    public DBTable(Connection conn) {
        super(conn);
    }
    @Column(type = SQLType.UUID, primaryKey = true)
    private UUID DBTable_id;

    @Column(type = SQLType.INT)
    @DefaultValue(defaultValue = "1")
    private int table_int;


}
