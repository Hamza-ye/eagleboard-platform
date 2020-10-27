package com.mass3d.hibernate.jsonb.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonJobParametersType extends JsonBinaryType
{
    static final ObjectMapper MAPPER = new ObjectMapper();

    static
    {
        MAPPER.enableDefaultTyping(); //TODO remove?
        MAPPER.setSerializationInclusion( JsonInclude.Include.NON_NULL );
    }

    @Override
    protected ObjectMapper getResultingMapper()
    {
        return MAPPER;
    }
}
