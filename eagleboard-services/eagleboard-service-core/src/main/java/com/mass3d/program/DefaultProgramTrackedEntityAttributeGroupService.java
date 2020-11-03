package com.mass3d.program;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramTrackedEntityAttributeGroupService" )
public class DefaultProgramTrackedEntityAttributeGroupService
    implements ProgramTrackedEntityAttributeGroupService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final IdentifiableObjectStore<ProgramTrackedEntityAttributeGroup> attributeGroupStore;

    public DefaultProgramTrackedEntityAttributeGroupService(@Qualifier( "com.mass3d.program.ProgramTrackedEntityAttributeGroupStore" ) IdentifiableObjectStore<ProgramTrackedEntityAttributeGroup> attributeGroupStore) {
        this.attributeGroupStore = attributeGroupStore;
    }

    // -------------------------------------------------------------------------
    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addProgramTrackedEntityAttributeGroup( ProgramTrackedEntityAttributeGroup attributeGroup )
    {
        attributeGroupStore.save( attributeGroup );

        return attributeGroup.getId();
    }

    @Override
    @Transactional
    public void deleteProgramTrackedEntityAttributeGroup( ProgramTrackedEntityAttributeGroup attributeGroup )
    {
        attributeGroupStore.delete( attributeGroup );
    }

    @Override
    @Transactional
    public void updateProgramTrackedEntityAttributeGroup( ProgramTrackedEntityAttributeGroup attributeGroup )
    {
        attributeGroupStore.update( attributeGroup );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramTrackedEntityAttributeGroup getProgramTrackedEntityAttributeGroup( long id )
    {
        return attributeGroupStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramTrackedEntityAttributeGroup getProgramTrackedEntityAttributeGroup( String uid )
    {
        return attributeGroupStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramTrackedEntityAttributeGroup> getAllProgramTrackedEntityAttributeGroups()
    {
        return attributeGroupStore.getAll();
    }
}
