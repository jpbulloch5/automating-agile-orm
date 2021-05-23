package exceptions;

public class DBConnectionException extends Exception{
    public DBConnectionException(String str) {
        super(str);
    }
}
