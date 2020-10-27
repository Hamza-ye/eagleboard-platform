package com.mass3d.hibernate.jsonb.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;

public class JsonSetBinaryType
    extends JsonBinaryType
{
    static final ObjectMapper MAPPER = new ObjectMapper();

    static
    {
        MAPPER.setSerializationInclusion( JsonInclude.Include.NON_NULL );
    }

    @Override
    protected ObjectMapper getResultingMapper()
    {
        return MAPPER;
    }

    @Override
    protected JavaType getResultingJavaType( Class<?> returnedClass )
    {
        return MAPPER.getTypeFactory().constructCollectionType( Set.class, returnedClass );
    }
}
