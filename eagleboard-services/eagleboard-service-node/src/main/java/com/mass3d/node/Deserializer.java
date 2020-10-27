package com.mass3d.node;

import java.io.InputStream;
import java.util.List;

public interface Deserializer<T>
{
    List<String> contentTypes();

    T deserialize(InputStream inputStream) throws Exception;
}
