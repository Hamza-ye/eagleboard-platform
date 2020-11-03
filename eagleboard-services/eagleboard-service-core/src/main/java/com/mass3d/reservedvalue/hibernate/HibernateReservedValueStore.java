package com.mass3d.reservedvalue.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.common.Objects.TRACKEDENTITYATTRIBUTE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.mass3d.common.Objects;
import com.mass3d.hibernate.HibernateGenericStore;
import com.mass3d.jdbc.batchhandler.ReservedValueBatchHandler;
import com.mass3d.reservedvalue.ReservedValue;
import com.mass3d.reservedvalue.ReservedValueStore;
import org.hisp.quick.BatchHandler;
import org.hisp.quick.BatchHandlerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository( "com.mass3d.reservedvalue.ReservedValueStore" )
public class HibernateReservedValueStore
    extends HibernateGenericStore<ReservedValue>
    implements ReservedValueStore
{
    private final BatchHandlerFactory batchHandlerFactory;

    public HibernateReservedValueStore( SessionFactory sessionFactory, JdbcTemplate jdbcTemplate,
        ApplicationEventPublisher publisher, BatchHandlerFactory batchHandlerFactory )
    {
        super( sessionFactory, jdbcTemplate, publisher, ReservedValue.class, false );

        checkNotNull( batchHandlerFactory );

        this.batchHandlerFactory = batchHandlerFactory;
    }

    @Override
    public List<ReservedValue> reserveValues( ReservedValue reservedValue,
        List<String> values )
    {
        List<ReservedValue> toAdd = getGeneratedValues( reservedValue, values );

        BatchHandler<ReservedValue> batchHandler = batchHandlerFactory
            .createBatchHandler( ReservedValueBatchHandler.class ).init();

        toAdd.forEach( rv -> batchHandler.addObject( rv ) );
        batchHandler.flush();

        return toAdd;
    }

    @Override
    public List<ReservedValue> reserveValuesJpa( ReservedValue reservedValue, List<String> values )
    {
        List<ReservedValue> toAdd = getGeneratedValues( reservedValue, values );
        toAdd.forEach( rv -> save( rv ) );
        return toAdd;
    }

    /**
     * Generates a list of reserved values based on the given input.
     *
     * @param reservedValue the reserved value.
     * @param values the values to reserve.
     * @return a list of {@link ReservedValue}.
     */
    private List<ReservedValue> getGeneratedValues( ReservedValue reservedValue, List<String> values )
    {
        List<String> availableValues = getIfAvailable( reservedValue, values );

        List<ReservedValue> generatedValues = new ArrayList<>();

        availableValues.forEach( ( value ) -> {

            ReservedValue rv = new ReservedValue(
                reservedValue.getOwnerObject(),
                reservedValue.getOwnerUid(),
                reservedValue.getKey(),
                value,
                reservedValue.getExpiryDate()
            );

            rv.setCreated( reservedValue.getCreated() );

            generatedValues.add( rv );
        } );

        return generatedValues;
    }


    @Override
    public List<ReservedValue> getIfReservedValues( ReservedValue reservedValue,
        List<String> values )
    {
        String hql = "from ReservedValue rv where rv.ownerObject =:ownerObject and rv.ownerUid =:ownerUid " +
            "and rv.key =:key and rv.value in :values";

        return getQuery( hql )
            .setParameter( "ownerObject", reservedValue.getOwnerObject() )
            .setParameter( "ownerUid", reservedValue.getOwnerUid() )
            .setParameter( "key", reservedValue.getKey() )
            .setParameter( "values", values )
            .getResultList();
    }

    @Override
    public int getNumberOfUsedValues( ReservedValue reservedValue )
    {
        Query<Long> query = getTypedQuery( "SELECT count(*) FROM ReservedValue WHERE owneruid = :uid AND key = :key" );

        Long count = query.setParameter( "uid", reservedValue.getOwnerUid() )
            .setParameter( "key", reservedValue.getKey() )
            .getSingleResult();


        if ( Objects.valueOf( reservedValue.getOwnerObject() ).equals( TRACKEDENTITYATTRIBUTE ) )
        {
            Query<Long> attrQuery = getTypedQuery(
            "SELECT count(*) " +
                "FROM TrackedEntityAttributeValue " +
                "WHERE attribute = " +
                "( FROM TrackedEntityAttribute " +
                "WHERE uid = :uid ) " +
                "AND value LIKE :value " );

            count += attrQuery.setParameter( "uid", reservedValue.getOwnerUid() )
            .setParameter( "value", reservedValue.getValue() )
            .getSingleResult();
        }

        return count.intValue();
    }

    @Override
    public void removeExpiredReservations()
    {
        getQuery( "DELETE FROM ReservedValue WHERE expiryDate < :now" )
            .setParameter( "now", new Date() )
            .executeUpdate();
    }

    @Override
    public boolean useReservedValue( String ownerUID, String value )
    {
        return getQuery( "DELETE FROM ReservedValue WHERE owneruid = :uid AND value = :value" )
            .setParameter( "uid", ownerUID )
            .setParameter( "value", value )
            .executeUpdate() == 1;
    }

    @Override
    public void deleteReservedValueByUid( String uid )
    {
        getQuery( "DELETE FROM ReservedValue WHERE owneruid = :uid" )
            .setParameter( "uid", uid )
            .executeUpdate();
    }

    @Override
    public boolean isReserved( String ownerObject, String ownerUID, String value )
    {
        String hql = "from ReservedValue rv where rv.ownerObject =:ownerObject and rv.ownerUid =:ownerUid " +
            "and rv.value =:value";

        return !getQuery( hql )
            .setParameter( "ownerObject", ownerObject )
            .setParameter( "ownerUid", ownerUID )
            .setParameter( "value", value )
            .getResultList()
            .isEmpty();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private List<String> getIfAvailable( ReservedValue reservedValue, List<String> values )
    {
        List<String> reservedValues = getIfReservedValues( reservedValue, values ).stream()
            .map( ReservedValue::getValue )
            .collect( Collectors.toList() );

        values.removeAll( reservedValues );

        // All values supplied is unavailable
        if ( values.isEmpty() )
        {
            return values;
        }

        if ( Objects.valueOf( reservedValue.getOwnerObject() ).equals( TRACKEDENTITYATTRIBUTE ) )
        {
            values.removeAll( getUntypedSqlQuery(
                "SELECT value FROM trackedentityattributevalue WHERE trackedentityattributeid = (SELECT trackedentityattributeid FROM trackedentityattribute WHERE uid = ?1) AND value IN ?2" )
                .setParameter( 1, reservedValue.getOwnerUid() )
                .setParameter( 2, values )
                .list() );
        }

        return values;

    }
}
