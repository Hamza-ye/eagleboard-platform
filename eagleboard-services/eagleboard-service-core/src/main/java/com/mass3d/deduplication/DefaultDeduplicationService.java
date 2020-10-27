package com.mass3d.deduplication;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.deduplication.DeduplicationService" )
public class DefaultDeduplicationService
    implements DeduplicationService
{

    private final PotentialDuplicateStore potentialDuplicateStore;

    public DefaultDeduplicationService( PotentialDuplicateStore potentialDuplicateStore )
    {
        this.potentialDuplicateStore = potentialDuplicateStore;
    }

    @Override
    @Transactional
    public long addPotentialDuplicate( PotentialDuplicate potentialDuplicate )
    {
        potentialDuplicateStore.save( potentialDuplicate );
        return potentialDuplicate.getId();
    }

    @Override
    @Transactional( readOnly = true )
    public PotentialDuplicate getPotentialDuplicateById( long id )
    {
        return potentialDuplicateStore.get( id );
    }

    @Override
    @Transactional( readOnly = true )
    public PotentialDuplicate getPotentialDuplicateByUid( String uid )
    {
        return potentialDuplicateStore.getByUid( uid );
    }

    @Override
    @Transactional( readOnly = true )
    public List<PotentialDuplicate> getAllPotentialDuplicates()
    {
        return potentialDuplicateStore.getAll();
    }

    @Override
    @Transactional
    public void markPotentialDuplicateInvalid( PotentialDuplicate potentialDuplicate )
    {
        potentialDuplicate.setStatus( DeduplicationStatus.INVALID );
        potentialDuplicateStore.update( potentialDuplicate );
    }

    @Override
    @Transactional( readOnly = true )
    public int countPotentialDuplicates( PotentialDuplicateQuery query )
    {

        return potentialDuplicateStore.getCountByQuery( query );
    }

    @Override
    @Transactional( readOnly = true )
    public boolean exists( PotentialDuplicate potentialDuplicate )
    {
        return potentialDuplicateStore.exists( potentialDuplicate );
    }

    @Override
    @Transactional( readOnly = true )
    public List<PotentialDuplicate> getAllPotentialDuplicates( PotentialDuplicateQuery query )
    {
        return potentialDuplicateStore.getAllByQuery( query );
    }

    @Override
    @Transactional
    public void deletePotentialDuplicate( PotentialDuplicate potentialDuplicate )
    {
        potentialDuplicateStore.delete( potentialDuplicate );
    }
}
