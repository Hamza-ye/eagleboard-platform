package com.mass3d.security;

public interface ActionAccessResolver
{
    boolean hasAccess(String namespace, String name);
}
