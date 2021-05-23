package annotations;

import enums.SQLType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String columnName();

    //SQL data type the field will map to
    SQLType type();

    //add back in if there's time, not necessary for MVP
    //boolean unique() default false;

    boolean nonNull() default false;

    String defaultValue() default "";

    int length() default -1;


}
