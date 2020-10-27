package com.mass3d.node;

import java.io.OutputStream;
import java.util.List;

public interface Serializer<T>
{
    List<String> contentTypes();

    void serialize(T object, OutputStream outputStream) throws Exception;
}
