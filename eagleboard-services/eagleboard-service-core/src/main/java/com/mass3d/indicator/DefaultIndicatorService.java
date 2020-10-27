package com.mass3d.indicator;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.indicator.IndicatorService" )
public class DefaultIndicatorService
    implements IndicatorService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final IndicatorStore indicatorStore;

    private final IdentifiableObjectStore<IndicatorType> indicatorTypeStore;

    private final IdentifiableObjectStore<IndicatorGroup> indicatorGroupStore;

    private final IdentifiableObjectStore<IndicatorGroupSet> indicatorGroupSetStore;

    public DefaultIndicatorService( IndicatorStore indicatorStore,
        @Qualifier( "com.mass3d.indicator.IndicatorTypeStore" ) IdentifiableObjectStore<IndicatorType> indicatorTypeStore,
        @Qualifier( "com.mass3d.indicator.IndicatorGroupStore" ) IdentifiableObjectStore<IndicatorGroup> indicatorGroupStore,
        @Qualifier( "com.mass3d.indicator.IndicatorGroupSetStore" ) IdentifiableObjectStore<IndicatorGroupSet> indicatorGroupSetStore )
    {
        checkNotNull( indicatorStore );
        checkNotNull( indicatorTypeStore );
        checkNotNull( indicatorGroupStore );
        checkNotNull( indicatorGroupSetStore );

        this.indicatorStore = indicatorStore;
        this.indicatorTypeStore = indicatorTypeStore;
        this.indicatorGroupStore = indicatorGroupStore;
        this.indicatorGroupSetStore = indicatorGroupSetStore;
    }

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addIndicator( Indicator indicator )
    {
        indicatorStore.save( indicator );

        return indicator.getId();
    }

    @Override
    @Transactional
    public void updateIndicator( Indicator indicator )
    {
        indicatorStore.update( indicator );
    }

    @Override
    @Transactional
    public void deleteIndicator( Indicator indicator )
    {
        indicatorStore.delete( indicator );
    }

    @Override
    @Transactional(readOnly = true)
    public Indicator getIndicator( long id )
    {
        return indicatorStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public Indicator getIndicator( String uid )
    {
        return indicatorStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Indicator> getAllIndicators()
    {
        return indicatorStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Indicator> getIndicatorsWithGroupSets()
    {
        return indicatorStore.getIndicatorsWithGroupSets();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Indicator> getIndicatorsWithoutGroups()
    {
        return indicatorStore.getIndicatorsWithoutGroups();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Indicator> getIndicatorsWithDataSets()
    {
        return indicatorStore.getIndicatorsWithDataSets();
    }

    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addIndicatorType( IndicatorType indicatorType )
    {
        indicatorTypeStore.save( indicatorType );

        return indicatorType.getId();
    }

    @Override
    @Transactional
    public void updateIndicatorType( IndicatorType indicatorType )
    {
        indicatorTypeStore.update( indicatorType );
    }

    @Override
    @Transactional
    public void deleteIndicatorType( IndicatorType indicatorType )
    {
        indicatorTypeStore.delete( indicatorType );
    }

    @Override
    @Transactional(readOnly = true)
    public IndicatorType getIndicatorType( long id )
    {
        return indicatorTypeStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public IndicatorType getIndicatorType( String uid )
    {
        return indicatorTypeStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndicatorType> getAllIndicatorTypes()
    {
        return indicatorTypeStore.getAll();
    }

    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        indicatorGroupStore.save( indicatorGroup );

        return indicatorGroup.getId();
    }

    @Override
    @Transactional
    public void updateIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        indicatorGroupStore.update( indicatorGroup );
    }

    @Override
    @Transactional
    public void deleteIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        indicatorGroupStore.delete( indicatorGroup );
    }

    @Override
    @Transactional(readOnly = true)
    public IndicatorGroup getIndicatorGroup( long id )
    {
        return indicatorGroupStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public IndicatorGroup getIndicatorGroup( String uid )
    {
        return indicatorGroupStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndicatorGroup> getAllIndicatorGroups()
    {
        return indicatorGroupStore.getAll();
    }

    // -------------------------------------------------------------------------
    // IndicatorGroupSet
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addIndicatorGroupSet( IndicatorGroupSet groupSet )
    {
        indicatorGroupSetStore.save( groupSet );

        return groupSet.getId();
    }

    @Override
    @Transactional
    public void updateIndicatorGroupSet( IndicatorGroupSet groupSet )
    {
        indicatorGroupSetStore.update( groupSet );
    }

    @Override
    @Transactional
    public void deleteIndicatorGroupSet( IndicatorGroupSet groupSet )
    {
        indicatorGroupSetStore.delete( groupSet );
    }

    @Override
    @Transactional(readOnly = true)
    public IndicatorGroupSet getIndicatorGroupSet( long id )
    {
        return indicatorGroupSetStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public IndicatorGroupSet getIndicatorGroupSet( String uid )
    {
        return indicatorGroupSetStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndicatorGroupSet> getAllIndicatorGroupSets()
    {
        return indicatorGroupSetStore.getAll();
    }
}
