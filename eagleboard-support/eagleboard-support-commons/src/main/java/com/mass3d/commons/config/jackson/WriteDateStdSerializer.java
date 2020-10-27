package com.mass3d.commons.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Date;
import com.mass3d.util.DateUtils;

public class WriteDateStdSerializer extends JsonSerializer<Date>
{
    @Override
    public void serialize( Date date, JsonGenerator generator, SerializerProvider provider ) throws IOException
    {
        generator.writeString( DateUtils.getIso8601NoTz( date ) );
    }
}
