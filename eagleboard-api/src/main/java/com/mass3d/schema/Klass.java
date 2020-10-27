package com.mass3d.schema;

/**
 * Simple interface for classes that exposes a class containment. I.e. a Property have a property class.
 *
 */
public interface Klass
{
    Class<?> getKlass();
}
