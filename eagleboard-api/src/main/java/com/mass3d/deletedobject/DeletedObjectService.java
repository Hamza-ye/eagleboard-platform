package com.mass3d.deletedobject;

import java.util.List;

public interface DeletedObjectService
{
    void addDeletedObject(DeletedObject deletedObject);

    void deleteDeletedObject(DeletedObject deletedObject);

    void deleteDeletedObjects(DeletedObjectQuery query);

    List<DeletedObject> getDeletedObjectsByKlass(String klass);

    int countDeletedObjects();

    int countDeletedObjects(DeletedObjectQuery query);

    List<DeletedObject> getDeletedObjects();

    List<DeletedObject> getDeletedObjects(DeletedObjectQuery query);
}
