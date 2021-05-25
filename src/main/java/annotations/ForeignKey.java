package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {
    //Indicates this field is a UUID and should be associated with the UUID PK of named table
    String referencedTable();
    //String referencedTableID() default "";
}
