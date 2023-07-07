package model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
    String value();
}

@Target(ElementType.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
    String scope();
}

@Target(ElementType.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model {
    String admin();
}