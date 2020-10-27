package com.mass3d.dataelement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.mass3d.common.GenericDimensionalObjectStore;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.common.ValueType;
import com.mass3d.period.PeriodType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.dataelement.DataElementService" )
public class DefaultDataElementService
    implements DataElementService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementStore dataElementStore;

    private IdentifiableObjectStore<DataElementGroup> dataElementGroupStore;

    private GenericDimensionalObjectStore<DataElementGroupSet> dataElementGroupSetStore;

    public DefaultDataElementService( DataElementStore dataElementStore,
        IdentifiableObjectStore<DataElementGroup> dataElementGroupStore,
        GenericDimensionalObjectStore<DataElementGroupSet> dataElementGroupSetStore )
    {
        checkNotNull( dataElementStore  );
        checkNotNull( dataElementGroupStore  );
        checkNotNull( dataElementGroupSetStore  );

        this.dataElementStore = dataElementStore;
        this.dataElementGroupStore = dataElementGroupStore;
        this.dataElementGroupSetStore = dataElementGroupSetStore;
    }

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addDataElement( DataElement dataElement )
    {
        dataElementStore.save( dataElement );

        return dataElement.getId();
    }

    @Override
    @Transactional
    public void updateDataElement( DataElement dataElement )
    {
        dataElementStore.update( dataElement );
    }

    @Override
    @Transactional
    public void deleteDataElement( DataElement dataElement )
    {
        dataElementStore.delete( dataElement );
    }

    @Override
    @Transactional
    public DataElement getDataElement( long id )
    {
        return dataElementStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public DataElement getDataElement( String uid )
    {
        return dataElementStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public DataElement getDataElementByCode( String code )
    {
        return dataElementStore.getByCode( code );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getAllDataElements()
    {
        return dataElementStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getAllDataElementsByValueType( ValueType valueType )
    {
        return dataElementStore.getDataElementsByValueType( valueType );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getDataElementsByZeroIsSignificant( boolean zeroIsSignificant )
    {
        return dataElementStore.getDataElementsByZeroIsSignificant( zeroIsSignificant );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getDataElementsByPeriodType( final PeriodType periodType )
    {
        return getAllDataElements().stream().filter( p -> p.getPeriodType() != null && p.getPeriodType().equals( periodType ) ).collect( Collectors
            .toList() );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getDataElementsWithoutDataSets()
    {
        return dataElementStore.getDataElementsWithoutDataSets();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getDataElementsWithDataSets()
    {
        return dataElementStore.getDataElementsWithDataSets();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElement> getDataElementsByAggregationLevel( int aggregationLevel )
    {
        return dataElementStore.getDataElementsByAggregationLevel( aggregationLevel );
    }

    @Override
    @Transactional
    public long addDataElementGroup( DataElementGroup dataElementGroup )
    {
        dataElementGroupStore.save( dataElementGroup );

        return dataElementGroup.getId();
    }

    @Override
    @Transactional
    public void updateDataElementGroup( DataElementGroup dataElementGroup )
    {
        dataElementGroupStore.update( dataElementGroup );
    }

    @Override
    @Transactional
    public void deleteDataElementGroup( DataElementGroup dataElementGroup )
    {
        dataElementGroupStore.delete( dataElementGroup );
    }

    @Override
    @Transactional
    public DataElementGroup getDataElementGroup( long id )
    {
        return dataElementGroupStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElementGroup> getDataElementGroupsByUid( Collection<String> uids )
    {
        return dataElementGroupStore.getByUid( uids );
    }

    @Override
    @Transactional(readOnly = true)
    public DataElementGroup getDataElementGroup( String uid )
    {
        return dataElementGroupStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElementGroup> getAllDataElementGroups()
    {
        return dataElementGroupStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DataElementGroup getDataElementGroupByName( String name )
    {
        List<DataElementGroup> dataElementGroups = dataElementGroupStore.getAllEqName( name );

        return !dataElementGroups.isEmpty() ? dataElementGroups.get( 0 ) : null;
    }

    // -------------------------------------------------------------------------
    // DataElementGroupSet
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addDataElementGroupSet( DataElementGroupSet groupSet )
    {
        dataElementGroupSetStore.save( groupSet );

        return groupSet.getId();
    }

    @Override
    @Transactional
    public void updateDataElementGroupSet( DataElementGroupSet groupSet )
    {
        dataElementGroupSetStore.update( groupSet );
    }

    @Override
    @Transactional
    public void deleteDataElementGroupSet( DataElementGroupSet groupSet )
    {
        dataElementGroupSetStore.delete( groupSet );
    }

    @Override
    @Transactional(readOnly = true)
    public DataElementGroupSet getDataElementGroupSet( long id )
    {
        return dataElementGroupSetStore.get( id );
    }

    @Override
    @Transactional(readOnly = true)
    public DataElementGroupSet getDataElementGroupSet( String uid )
    {
        return dataElementGroupSetStore.getByUid( uid );
    }

    @Override
    @Transactional(readOnly = true)
    public DataElementGroupSet getDataElementGroupSetByName( String name )
    {
        List<DataElementGroupSet> dataElementGroupSets = dataElementGroupSetStore.getAllEqName( name );

        return !dataElementGroupSets.isEmpty() ? dataElementGroupSets.get( 0 ) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataElementGroupSet> getAllDataElementGroupSets()
    {
        return dataElementGroupSetStore.getAll();
    }

}
