package com.mass3d.security.authority;

import com.mass3d.schema.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class SchemaAuthoritiesProvider
    implements SystemAuthoritiesProvider
{
    private SchemaService schemaService;

    public SchemaAuthoritiesProvider( SchemaService schemaService )
    {
        this.schemaService = schemaService;
    }

    @Override
    public Collection<String> getSystemAuthorities()
    {
        return schemaService.collectAuthorities();
    }
}
