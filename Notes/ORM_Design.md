# ORM Design

### SQL
The ORM needs to abstract away all SQL syntax from the user. This means we need a 
way to handle anything that might come up. 
- Create tables
- Altering tables
- Handle PK <--> FK relations
- Insert rows
- Read rows
- Update rows
- Delete rows

### Connections
JDBC logic must also be abstracted away from the user, so ORM should handle connections. Connection 
pooling is one of the bonus features. 


### Transactions
Supporting basic transaction management (begin, commit, savepoint, rollback) is a bonus feature.

### Normalization
2NF says each table should use a single-column primary key. I used UUIDs in my p0 and find 
that to be a useful and easy solution. 


### Database initialization:
An application using the custom ORM should have an initialization step before any CRUD takes place. 
The init step sets up the connection, verifies tables, creates tables as necessary 
(based on annotation reflection on POJOs), and establishes a connection pool.
 
Reflection is not necessarily a requirement for project, so we could force the user to manually
register all classes during init. 

modifying existing tables:
add non-null column to existing table with entries: need to add default value to fill in the existing rows.



### Defining tables
For all FK's simply force a default value. This takes care of most FK alteration cases.
 
(OPTIONAL) - build time validation of table definitions, throw build errors instead of waiting to
throw runtime errors in case of things like mutually exclusive !nullable + unique, or !nullable without
default.
 
Use annotations to describe a table and columns in a POJO or Repository class.
- @table - this class represents a table
- @column - this field gets stored in a column
  - type=(type) - attribute describing the data type. Map like to like java <--> SQL
  - foreignKey=(tablename) - attribute telling ORM that this field is a UUID and points to a
    UUID PK in another table 
  - unique=(true|false) - column values are UNIQUE
  - nullable=(true|false) - column is NOT NULL 
  - default=(value) set default value for non-null columns. NOT NULL requires default, 
    which makes nullable and unique mutually exclusive
  

Repository (POJO) classes should extend a super class which contains the CRUD behavior.
