package com.mass3d.node;

import java.io.OutputStream;
import java.util.List;
import com.mass3d.node.types.RootNode;

public interface NodeSerializer extends Serializer<RootNode>
{
    @Override
    List<String> contentTypes();

    @Override
    void serialize(RootNode rootNode, OutputStream outputStream) throws Exception;
}
