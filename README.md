# Team e - P1 - Custom Object Relational Mapping Framework

## Description:
As of 1.0, eORM, the team e custom ORM, supports basic CRUD functionality, basic connections to a postgresql database,
and basic programmatic table creation via annotations.

## Annotations:
### @Table
- Applies to: Class
- Required Parameter: String tableName
      
`@Table(tableName = "table_name")`

The @Table annotation informs eORM that the following class is a table, 
and specifies the table name in the database. This requires a corresponding field
in the class of type UUID which is named "<TABLENAME>_id" with the @Table 
annotation which includes the primaryKey parameter to be true.

### @Column
- Applies to: Field
- Required Parameter: SQLType type
- Parameter: boolean primaryKey
- Parameter: boolean nonNull
- Parameter: int length

`@Column(type = SQLType.UUID, primaryKey = true)`
 
`@Column(type = SQLType.VARCHAR, nonNull = true, length = 255)`

The @Column annotation informs eORM this field is to be represented as a column 
within the table. At least one column is required n every table, a primary key
of the type UUID with the same name as the table + "_id".

### @DefaultValue
- Applies to: Field
- Required Parameter: String defaultValue

`@DefaultValue(defaultValue = "def")`

The @DefaultValue annotation informs eORM that this table column should have 
a default value, which is set with the parameter defaultValue.


### @ForeignKey
- Applies to: Field
- Required Parameter: String referencedTable

`@ForeignKey(referencedTable = "table_name")`

The @ForeignKey annotation informs eORM that this table has a foreign key relation with another
table, which is identified in the referencedTable parameter. This parameter should match
the target table's table name as mentioned in the @Table annotation. The foreign key always
points to the referenced table's primary key.

## SQLType Enum:
As of 1.0 the following types can be used in the database:
 
| SQL type | Java type |
| -------- | --------- |
| UUID     | UUID      |
| BOOL     | boolean   |
| INT      | int       |
| BIGINT   | long      |
| NUMERIC  | double    |
| VARCHAR  | String    |


## Connecting:
Connect to the database with the following information:
- host
- port
- db
- schema
- username
- password
- driver
 
 These parameters will form a connection URL used by eORM to establish a Connection object.
 `jdbc:postgresql://[host]:[port]/[db]?currentSchema=[schema]`
  It uses the username and password provided to authenticate, and uses the driver to provided
  for the connection. If unsure, use driver: `org.postgresql.Driver`. Please avoid hard 
  coding these values.
 
 To establish a connection, use the static method:
 
 `Connection conn = eorm.utils.ConnectionFactory(host, port, db, schema, user, password, driver)`
 
 
## Table Initialization:
Before a table exists in the database and can be used, it must be initialized. 
This only needs to be done once for each table, subsequent calls to initialize() are ignored.

```
@Table(tableName = "table")
Class Table extends Repository {
    @Column(type = SQLType.UUID, primaryKey = true)
    private UUID table_id;
}
Table table = new Table();
table.initialize();
```

Tables must extend the Repository class, which contains the initialize and CRUD methods.

## CRUD:
The create, read, update, delete functionality is handled bythe following methods 
inherited from Repository: 
- `public void save()`
- `public void refresh()`
- `public void delete()`
- `public static List<Repository> query()`

Save() will write the values in the object's @Column fields to the @Table table. This is based 
on the UUID primary key. If the entry already exists, it is updated. Otherwise, it is inserted.
 
Refresh() will load the values into the @Column fields from the @Table table in the database.
 
Delete() will remove the entry matching the UUID primary key from the @Table table.
 
Query() is static, and returns a list of results which represent all of the data in the @Table
table.


