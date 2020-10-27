package com.mass3d.dxf2.metadata.collection;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.webmessage.WebMessageException;

public interface CollectionService
{
    void addCollectionItems(IdentifiableObject object, String propertyName,
        List<IdentifiableObject> objects) throws Exception;

    void delCollectionItems(IdentifiableObject object, String propertyName,
        List<IdentifiableObject> objects) throws Exception;

    void clearCollectionItems(IdentifiableObject object,
        String pvProperty) throws WebMessageException, InvocationTargetException, IllegalAccessException;
}
