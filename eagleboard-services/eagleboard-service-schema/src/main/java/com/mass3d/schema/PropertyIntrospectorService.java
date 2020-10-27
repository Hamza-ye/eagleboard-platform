package com.mass3d.schema;

import java.util.List;
import java.util.Map;

public interface PropertyIntrospectorService
{
    /**
     * Returns all exposed properties on wanted class.
     *
     * @param klass Class to get properties for
     * @return List of properties for Class
     */
    List<Property> getProperties(Class<?> klass);

    /**
     * Returns properties as a map property-name => Property class
     *
     * @param klass Class to get properties for
     * @return Map with key property-name and value Property
     */
    Map<String, Property> getPropertiesMap(Class<?> klass);

    // TODO should probably be moved out of PropertyIntrospectorService, useful other places also
    Class<?> getConcreteClass(Class<?> klass);
}
