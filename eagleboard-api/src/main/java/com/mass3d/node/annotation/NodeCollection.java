package com.mass3d.node.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@NodeAnnotation
public @interface NodeCollection {

  String value() default "";

  String namespace() default "";

  String itemName() default "";

  String itemNamespace() default "";

  boolean useWrapping() default true;

  boolean isWritable() default true;

  boolean isReadable() default true;
}
