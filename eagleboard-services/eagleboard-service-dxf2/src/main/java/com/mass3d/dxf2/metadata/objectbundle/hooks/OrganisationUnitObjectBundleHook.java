package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.comparator.OrganisationUnitParentCountComparator;
import com.mass3d.system.util.GeoUtils;
import org.springframework.stereotype.Component;

@Component
public class OrganisationUnitObjectBundleHook extends AbstractObjectBundleHook
{
    @Override
    public void preCommit( ObjectBundle objectBundle )
    {
        sortOrganisationUnits( objectBundle );
    }

    private void sortOrganisationUnits( ObjectBundle bundle )
    {
        List<IdentifiableObject> nonPersistedObjects = bundle.getObjects( OrganisationUnit.class, false );
        List<IdentifiableObject> persistedObjects = bundle.getObjects( OrganisationUnit.class, true );

        nonPersistedObjects.sort( new OrganisationUnitParentCountComparator() );
        persistedObjects.sort( new OrganisationUnitParentCountComparator() );
    }

    @Override
    public void postCommit( ObjectBundle bundle )
    {
        if ( !bundle.getObjectMap().containsKey( OrganisationUnit.class ) ) return;

        List<IdentifiableObject> objects = bundle.getObjectMap().get( OrganisationUnit.class );
        Map<String, Map<String, Object>> objectReferences = bundle.getObjectReferences( OrganisationUnit.class );

        Session session = sessionFactory.getCurrentSession();

        for ( IdentifiableObject identifiableObject : objects )
        {
            identifiableObject = bundle.getPreheat().get( bundle.getPreheatIdentifier(), identifiableObject );
            Map<String, Object> objectReferenceMap = objectReferences.get( identifiableObject.getUid() );

            if ( objectReferenceMap == null || objectReferenceMap.isEmpty() || !objectReferenceMap.containsKey( "parent" ) )
            {
                continue;
            }

            OrganisationUnit organisationUnit = (OrganisationUnit) identifiableObject;
            OrganisationUnit parentRef = (OrganisationUnit) objectReferenceMap.get( "parent" );
            OrganisationUnit parent = bundle.getPreheat().get( bundle.getPreheatIdentifier(), parentRef );

            organisationUnit.setParent( parent );
            session.update( organisationUnit );
        }
    }

    @Override
    public void preCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        setSRID( object );
    }

    @Override
    public void preUpdate( IdentifiableObject object, IdentifiableObject persistedObject, ObjectBundle bundle )
    {
        setSRID( object );
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> validate( T object, ObjectBundle bundle )
    {
        if ( object == null || !object.getClass().isAssignableFrom( OrganisationUnit.class ) )
        {
            return new ArrayList<>();
        }

        OrganisationUnit organisationUnit = ( OrganisationUnit ) object;

        List<ErrorReport> errors = new ArrayList<>();

        if ( organisationUnit.getClosedDate() != null && organisationUnit.getClosedDate().before( organisationUnit.getOpeningDate() ) )
        {
            errors.add( new ErrorReport( OrganisationUnit.class, ErrorCode.E4013 , organisationUnit.getClosedDate(), organisationUnit
                .getOpeningDate()) );
        }

        return errors;
    }

    private void setSRID( IdentifiableObject object )
    {
        if ( !OrganisationUnit.class.isInstance( object ) ) return;

        OrganisationUnit organisationUnit = ( OrganisationUnit ) object;

        if ( organisationUnit.getGeometry() != null )
        {
            organisationUnit.getGeometry().setSRID( GeoUtils.SRID );
        }
    }
}
