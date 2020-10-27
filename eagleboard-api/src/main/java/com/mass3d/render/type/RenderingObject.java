package com.mass3d.render.type;

public interface RenderingObject<T extends Enum<?>>
{
    String _TYPE = "type";

    T getType();

    void setType(T type);

    Class<T> getRenderTypeClass();
}
