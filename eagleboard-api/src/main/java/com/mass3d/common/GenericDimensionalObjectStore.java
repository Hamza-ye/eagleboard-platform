package com.mass3d.common;

import java.util.List;

public interface GenericDimensionalObjectStore<T>
    extends IdentifiableObjectStore<T>
{
    /**
     * Retrieves a List of dimensional objects.
     *
     * @param dataDimension indicates whether to fetch objects defined as dimensional.
     * @return a List of objects.
     */
    List<T> getByDataDimension( boolean dataDimension );

    /**
     * Retrieves a List of dimensional objects. Ignore ACL / sharing.
     *
     * @param dataDimension indicates whether to fetch objects defined as dimensional.
     * @return a List of objects.
     */
    List<T> getByDataDimensionNoAcl( boolean dataDimension );
}
