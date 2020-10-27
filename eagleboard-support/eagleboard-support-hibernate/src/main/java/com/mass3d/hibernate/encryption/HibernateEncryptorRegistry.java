package com.mass3d.hibernate.encryption;

import com.google.common.collect.Maps;
import org.jasypt.encryption.pbe.PBEStringEncryptor;

import java.util.Map;

/**
 * Singleton registry for all (named) Hibernate Encryptors.
 * {@link com.mass3d.hibernate.encryption.type.EncryptedStringUserType EncryptedStringUserType}
 * depends on this singleton to access the appropriate encryptor(s).
 *
 */
public final class HibernateEncryptorRegistry
{
    private static final HibernateEncryptorRegistry INSTANCE = new HibernateEncryptorRegistry();

    private final Map<String, PBEStringEncryptor> encryptors = Maps.newHashMap();

    private HibernateEncryptorRegistry()
    {   
    }

    /**
     * Returns the (singleton) instance of the registry.
     *
     * @return this registry.
     */
    public static HibernateEncryptorRegistry getInstance()
    {
        return INSTANCE;
    }

    /**
     * Registers the given {@link PBEStringEncryptor PBEStringEncryptors} by name.
     *
     * @param encryptors a map of names and encryptors.
     */
    public synchronized void setEncryptors( Map<String, PBEStringEncryptor> encryptors )
    {
        INSTANCE.encryptors.putAll( encryptors );
    }

    /**
     * Get encryptor from registry by name.
     *
     * @param name the name of the encryptor.
     * @return an instance of {@link PBEStringEncryptor} or null.
     */
    public PBEStringEncryptor getEncryptor( String name )
    {
        return INSTANCE.encryptors.get( name );
    }
}
