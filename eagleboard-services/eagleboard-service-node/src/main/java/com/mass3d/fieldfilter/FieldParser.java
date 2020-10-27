package com.mass3d.fieldfilter;

import java.util.Collection;
import java.util.List;

public interface FieldParser
{
    /**
     * Parses and writes out fieldMap with included/excluded properties.
     *
     * @param filter String to parse, can be used for both inclusion/exclusion
     * @return FieldMap with property name as key, and another FieldMap as value (recursive)
     * @see com.mass3d.fieldfilter.FieldMap
     */
    FieldMap parse(String filter);

    /**
     * Recursively add some field filtering to a field filter
     *
     * @param fieldFilter Field filter to modify
     * @param excludeFields Fields to add to the field filter
     * @return Modified field filter
     */
    List<String> modifyFilter(Collection<String> fieldFilter, Collection<String> excludeFields);
}
