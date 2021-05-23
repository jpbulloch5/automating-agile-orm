package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String columnName();

    public enum SQLType {
        UUID,       //UUID
        BOOL,       //java boolean - 1 bit
        INT,        //java int - 4 byte
        BIGINT,     //java long - 8 byte
        NUMERIC,    //java float/double with selectable precision
        VARCHAR    //variable length character string
    }

    //SQL data type the field will map to
    SQLType type() default SQLType.BOOL;

    boolean unique() default false;

    boolean nonNull() default false;

    int length() default -1;


}
