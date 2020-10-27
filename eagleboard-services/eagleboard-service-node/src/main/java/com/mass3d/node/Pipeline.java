package com.mass3d.node;

public interface Pipeline<T>
{
    T process(T object);
}
