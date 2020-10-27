package com.mass3d.hibernate.encryption.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import com.mass3d.hibernate.encryption.HibernateEncryptorRegistry;
import org.jasypt.encryption.pbe.PBEStringEncryptor;

/**
 * Hibernate {@link UserType} implementation which employs a {@link PBEStringEncryptor} to
 * perform transparent encryption/decryption of {@link String} properties.
 *
 * The employed encryptor is resolved from the {@link HibernateEncryptorRegistry}, which must be
 * set up with a named encryptor. The encryptor is resolved through the 'encryptor' parameter,
 * which looks up the given name in the registry.
 *
 * If no 'encryptor' parameter is given, or the given name does not resolve to a
 * {@link PBEStringEncryptor} in the {@link HibernateEncryptorRegistry}, an
 * {@link IllegalArgumentException} is thrown at initialization.
 *
 * This class implements a similar pattern to the encrypted types provided by the
 * org.jasypt.hibernate4 package, but serves to avoid this dependency (which breaks on Hibernate > 5.1.x).
 *
 */
public class EncryptedStringUserType
    implements UserType, ParameterizedType
{
    public static final String PARAMETER_ENCRYPTOR = "encryptor";

    private static final int[] sqlTypes = new int[] { Types.VARCHAR };

    private String encryptorName = null;

    private PBEStringEncryptor encryptor = null;

    @Override
    public int[] sqlTypes()
    {
        return sqlTypes.clone();
    }

    @Override
    public Class<?> returnedClass()
    {
        return String.class;
    }

    @Override
    public boolean equals( Object x, Object y )
        throws HibernateException
    {
        return x == y || ( x != null && y != null && x.equals( y ) );
    }

    @Override
    public int hashCode( final Object x )
        throws HibernateException
    {
        return x.hashCode();
    }

       @Override
    public Object nullSafeGet( ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner )
        throws HibernateException, SQLException
    {
        ensureEncryptorInit();

        String value = rs.getString( names[0] );

        return rs.wasNull() ? null : encryptor.decrypt( value );
    }

    @Override
    public void nullSafeSet( PreparedStatement st, Object value, int index, SharedSessionContractImplementor session )
        throws HibernateException, SQLException
    {
        ensureEncryptorInit();

        if ( value == null )
        {
            st.setNull( index, Types.VARCHAR );
        }
        else
        {
            st.setString( index, encryptor.encrypt( value.toString() ) );
        }
    }

    @Override
    public Object deepCopy( final Object value )
        throws HibernateException
    {
        return value;
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public Serializable disassemble( Object value ) throws HibernateException
    {
        return value == null ? null : (Serializable) deepCopy( value );
    }

    @Override
    public Object assemble( Serializable cached, Object owner ) throws HibernateException
    {
        return cached == null ? null : deepCopy( cached );
    }

    @Override
    public Object replace( Object original, Object target, Object owner ) throws HibernateException
    {
        return original;
    }

    @Override
    public void setParameterValues( Properties parameters )
    {
        this.encryptorName = parameters.getProperty( PARAMETER_ENCRYPTOR );

        if ( encryptorName == null )
        {
            throw new IllegalArgumentException(
                String.format( "Required parameter '%s' is not configured", PARAMETER_ENCRYPTOR ) );
        }
    }

    // Private methods

    private synchronized void ensureEncryptorInit()
    {
        if ( encryptor == null )
        {
            encryptor = HibernateEncryptorRegistry.getInstance().getEncryptor( encryptorName );
        }
    }
}
