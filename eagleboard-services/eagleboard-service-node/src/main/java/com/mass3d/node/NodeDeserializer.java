package com.mass3d.node;

import java.io.InputStream;
import java.util.List;
import com.mass3d.node.types.RootNode;

public interface NodeDeserializer extends Deserializer<RootNode>
{
    @Override
    List<String> contentTypes();

    @Override
    RootNode deserialize(InputStream inputStream) throws Exception;
}
