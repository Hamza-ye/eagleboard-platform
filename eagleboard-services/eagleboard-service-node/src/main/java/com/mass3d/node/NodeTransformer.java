package com.mass3d.node;

import java.util.List;

public interface NodeTransformer
{
    String name();

    Node transform(Node object, List<String> args);
}
