package com.mass3d.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Property annotation for setting min/max range.
 * <p/>
 * Behavior changes according to type:
 * <ul>
 * <ol>PropertyType.TEXT: min/max length of TEXT</ol>
 * <ol>PropertyType.COLLECTION: min/max size of collection</ol>
 * <ol>PropertyType.NUMBER: min/max values (only integer min/max currently allowed)</ol>
 * <ol>PropertyType.INTEGER: min/max values</ol>
 * </ul>
 * <p>
 * Be aware that this annotation overrides anything set in the schema, so it's possible to have
 * values here that goes beyond what the schema allows, and would result in error when trying to
 * save the object.
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyRange {

  double min() default 0;

  double max() default Double.MAX_VALUE;
}
