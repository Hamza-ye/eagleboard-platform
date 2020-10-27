package com.mass3d.deletedobject;

import java.util.List;

public interface DeletedObjectStore
{
    long save(DeletedObject deletedObject);

    void delete(DeletedObject deletedObject);

    void delete(DeletedObjectQuery query);

    List<DeletedObject> getByKlass(String klass);

    int count(DeletedObjectQuery query);

    List<DeletedObject> query(DeletedObjectQuery query);
}
