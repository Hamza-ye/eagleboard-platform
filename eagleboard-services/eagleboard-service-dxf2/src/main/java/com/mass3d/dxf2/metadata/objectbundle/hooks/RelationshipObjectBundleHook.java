package com.mass3d.dxf2.metadata.objectbundle.hooks;

import com.mass3d.common.IdentifiableObject;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.relationship.Relationship;
import com.mass3d.relationship.RelationshipItem;
import com.mass3d.relationship.RelationshipTypeService;
import com.mass3d.trackedentity.TrackedEntityInstanceStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component( "com.mass3d.dxf2.metadata.objectbundle.hooks.RelationshipObjectBundleHook" )
public class RelationshipObjectBundleHook
    extends AbstractObjectBundleHook
{

    @Autowired
    private TrackedEntityInstanceStore trackedEntityInstanceStore;

    @Autowired
    private RelationshipTypeService relationshipTypeService;

    @Override
    public void preCreate( IdentifiableObject object, ObjectBundle bundle )
    {
        if ( !Relationship.class.isInstance( object ) )
        {
            return;
        }

        Relationship relationship = (Relationship) object;

        handleRelationshipItem( relationship.getFrom() );
        sessionFactory.getCurrentSession().save( relationship.getFrom() );
        handleRelationshipItem( relationship.getTo() );
        sessionFactory.getCurrentSession().save( relationship.getTo() );

        relationship.setRelationshipType(
            relationshipTypeService.getRelationshipType( relationship.getRelationshipType().getUid() ) );
        sessionFactory.getCurrentSession().save( relationship.getRelationshipType() );
    }

    private void handleRelationshipItem( RelationshipItem relationshipItem )
    {
        if ( relationshipItem.getTrackedEntityInstance() != null )
        {
            relationshipItem.setTrackedEntityInstance(
                trackedEntityInstanceStore.getByUid( relationshipItem.getTrackedEntityInstance().getUid() ) );
        }
    }

}
