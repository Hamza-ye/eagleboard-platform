package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundleHook;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.preheat.PreheatService;
import com.mass3d.schema.MergeService;
import com.mass3d.schema.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractObjectBundleHook implements ObjectBundleHook
{
    @Autowired
    protected IdentifiableObjectManager manager;

    @Autowired
    protected PreheatService preheatService;

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    protected SchemaService schemaService;

    @Autowired
    protected MergeService mergeService;

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        return new ArrayList<>();
    }

    @Override
    public void preCommit( ObjectBundle bundle )
    {
    }

    @Override
    public void postCommit( ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void preTypeImport( Class<? extends IdentifiableObject> klass, List<T> objects, ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void postTypeImport( Class<? extends IdentifiableObject> klass, List<T> objects, ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void preCreate( T object, ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void postCreate( T persistedObject, ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void preUpdate( T object, T persistedObject, ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void postUpdate( T persistedObject, ObjectBundle bundle )
    {
    }

    @Override
    public <T extends IdentifiableObject> void preDelete( T persistedObject, ObjectBundle bundle )
    {
    }
}
