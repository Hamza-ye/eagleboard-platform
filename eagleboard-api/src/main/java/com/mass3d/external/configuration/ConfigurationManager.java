package com.mass3d.external.configuration;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @version $Id$
 */
public interface ConfigurationManager<T>
{    
    /**
     * Writes a configuration object to an XML file. The outputstream will
     * be closed after use.
     * 
     * @param configuration the configuration object.
     * @param out the outputstream to write to.
     */
    void setConfiguration(T configuration, OutputStream out, Class<?>... clazzes);

    /**
     * Reads a configuration object from an XML file. The inputstream will
     * be closed after use.
     *
     * @param in the inputstream to read from.
     * @param clazz the Class of the configuration object.
     * @return a configuration object.
     */
    T getConfiguration(InputStream in, Class<?> clazz, Class<?>... clazzes);
}
