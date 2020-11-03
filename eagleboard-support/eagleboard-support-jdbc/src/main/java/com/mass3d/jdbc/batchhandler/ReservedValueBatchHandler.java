package com.mass3d.jdbc.batchhandler;

import com.mass3d.reservedvalue.ReservedValue;
import org.hisp.quick.JdbcConfiguration;
import org.hisp.quick.batchhandler.AbstractBatchHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class ReservedValueBatchHandler
    extends AbstractBatchHandler<ReservedValue>
{
    public ReservedValueBatchHandler( JdbcConfiguration configuration )
    {
        super( configuration );
    }

    @Override
    public String getTableName()
    {
        return "reservedvalue";
    }

    @Override
    public String getAutoIncrementColumn()
    {
        return "reservedvalueid";
    }

    @Override
    public boolean isInclusiveUniqueColumns()
    {
        return true;
    }

    @Override
    public List<String> getIdentifierColumns()
    {
        return getStringList( "reservedvalueid" );
    }

    @Override
    public List<Object> getIdentifierValues( ReservedValue object )
    {
        return getObjectList( object.getId() );
    }

    @Override
    public List<String> getUniqueColumns()
    {
        return getStringList( "reservedvalueid", "ownerobject", "owneruid", "key", "value" );
    }

    @Override
    public List<Object> getUniqueValues( ReservedValue object )
    {
        return getObjectList(
            object.getId(),
            object.getOwnerObject(),
            object.getOwnerUid(),
            object.getKey(),
            object.getValue()
        );
    }

    @Override
    public List<String> getColumns()
    {
        return getStringList( "ownerobject", "owneruid", "key", "value", "expirydate", "created" );
    }

    @Override
    public List<Object> getValues( ReservedValue object )
    {
        return getObjectList(
            object.getOwnerObject(),
            object.getOwnerUid(),
            object.getKey(),
            object.getValue(),
            object.getExpiryDate(),
            object.getCreated()
        );
    }

    @Override
    public ReservedValue mapRow( ResultSet resultSet )
        throws SQLException
    {
        Calendar expires = Calendar.getInstance();

        ReservedValue rv = new ReservedValue();

        expires.setTime( resultSet.getDate( "expirydate" ) );

        rv.setId( resultSet.getInt( "reservedvalueid" ) );
        rv.setOwnerObject( resultSet.getString( "ownerobject" ) );
        rv.setOwnerUid( resultSet.getString( "ownerUid" ) );
        rv.setKey( resultSet.getString( "key" ) );
        rv.setValue( resultSet.getString( "value" ) );
        rv.setExpiryDate( expires.getTime() );
        rv.setCreated( resultSet.getDate( "created" ) );

        return rv;
    }
    
    @Override
    public String getIdSequenceName()
    {
        return "reservedvalue_sequence";
    }
}
