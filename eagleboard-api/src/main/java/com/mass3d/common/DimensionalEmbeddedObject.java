package com.mass3d.common;

import java.util.List;

public interface DimensionalEmbeddedObject
    extends EmbeddedObject
{
    int getId();

    DimensionalObject getDimension();

    List<? extends DimensionalItemObject> getItems();
}
