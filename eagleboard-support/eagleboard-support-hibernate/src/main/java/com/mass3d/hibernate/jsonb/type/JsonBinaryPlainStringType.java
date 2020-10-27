package com.mass3d.hibernate.jsonb.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.postgresql.util.PGobject;

/**
 * User defined type to handle dynamic json structures to be stored in jsonb.
 * Always deserializes into java Strings.
 *
 */
public class JsonBinaryPlainStringType extends JsonBinaryType
{
    @Override
    public Class<?> returnedClass()
    {
        return String.class;
    }

    @Override
    public Object nullSafeGet( ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner ) throws HibernateException, SQLException
    {
        final Object result = rs.getObject( names[0] );

        if ( !rs.wasNull() )
        {
            String content = null;

            if ( result instanceof String)
            {
                content = (String) result;
            }
            else if ( result instanceof PGobject )
            {
                content = ((PGobject) result).getValue();
            }

            // Other types currently ignored

            if ( content != null )
            {
                return content.toString();
            }
        }

        return null;
    }

    @Override
    public void nullSafeSet( PreparedStatement ps, Object value, int idx, SharedSessionContractImplementor session ) throws HibernateException, SQLException
    {
        if ( value == null )
        {
            ps.setObject( idx, null );
            return;
        }

        PGobject pg = new PGobject();
        pg.setType( "jsonb" );
        pg.setValue( value.toString() );

        ps.setObject( idx, pg );
    }

    @Override
    public Object deepCopy( Object value ) throws HibernateException
    {
        return value == null ? null : value.toString();
    }

    @Override
    public void setParameterValues( Properties parameters )
    {

    }

}
