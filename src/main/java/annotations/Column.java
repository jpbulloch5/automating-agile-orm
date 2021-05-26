package annotations;

import enums.SQLType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    SQLType type();
    boolean primaryKey() default false;
    boolean nonNull() default false;
    int length() default -1;
    //String defaultValue() default "";

}
