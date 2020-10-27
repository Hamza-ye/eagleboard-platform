package com.mass3d.hibernate.jsonb.type;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.hibernate.HibernateException;
import com.mass3d.programrule.ProgramRuleActionEvaluationEnvironment;

public class JsonProgramRuleEvaluationEnvironmentSetBinaryType
    extends JsonBinaryType
{
    public JsonProgramRuleEvaluationEnvironmentSetBinaryType()
    {
        super();
        writer = MAPPER.writerFor( new TypeReference<Set<ProgramRuleActionEvaluationEnvironment>>()
        {
        } );
        reader = MAPPER.readerFor( new TypeReference<Set<ProgramRuleActionEvaluationEnvironment>>()
        {
        } );
        returnedClass = ProgramRuleActionEvaluationEnvironment.class;
    }

    @Override
    protected void init( Class<?> klass )
    {
        returnedClass = klass;
        reader = MAPPER.readerFor( new TypeReference<Set<ProgramRuleActionEvaluationEnvironment>>()
        {
        } );
        writer = MAPPER.writerFor( new TypeReference<Set<ProgramRuleActionEvaluationEnvironment>>()
        {
        } );
    }

    @Override
    public Object deepCopy( Object value ) throws HibernateException
    {
        String json = convertObjectToJson( value );
        return convertJsonToObject( json );
    }

    /**
     * Serializes an object to JSON.
     *
     * @param object the object to convert.
     * @return JSON content.
     */
    @SuppressWarnings( "unchecked" )
    @Override
    protected String convertObjectToJson( Object object )
    {
        try
        {
            Set<ProgramRuleActionEvaluationEnvironment> environments =
                object == null ? Collections
                    .emptySet() : (Set<ProgramRuleActionEvaluationEnvironment>) object;

            return writer.writeValueAsString( environments );
        }
        catch ( IOException e )
        {
            throw new IllegalArgumentException( e );
        }
    }

    /**
     * Deserializes JSON content to an object.
     *
     * @param content the JSON content.
     * @return an object.
     */
    @Override
    public Object convertJsonToObject( String content )
    {
        try
        {
            return reader.readValue( content );
        }
        catch ( IOException e )
        {
            throw new IllegalArgumentException( e );
        }
    }
}
