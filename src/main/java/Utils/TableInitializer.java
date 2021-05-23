package Utils;

import annotations.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TableInitializer {


    public static void InitializeTable(Class<? extends Repository> repo) {
        if (repo.isAnnotationPresent(Table.class)) {
            System.out.println("Table annotation present!\n");
        }

        Annotation[] annotations = repo.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            System.out.println("Annotation: " + annotations[i].toString());

        }

        Field[] fields = repo.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Annotation[] fieldAnnotations = fields[i].getAnnotations();
            for (int j = 0; j < fieldAnnotations.length; j++) {
                System.out.println("Field annotation[" + fields[i].getName() + "]: " + fieldAnnotations[j].toString());
            }
            System.out.println();
        }

    }
}
