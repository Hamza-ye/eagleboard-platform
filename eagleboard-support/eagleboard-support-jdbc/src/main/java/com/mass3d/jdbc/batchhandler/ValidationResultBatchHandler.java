package com.mass3d.jdbc.batchhandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.mass3d.validation.ValidationResult;
import org.hisp.quick.JdbcConfiguration;
import org.hisp.quick.batchhandler.AbstractBatchHandler;

public class ValidationResultBatchHandler
    extends AbstractBatchHandler<ValidationResult>
{
    public ValidationResultBatchHandler( JdbcConfiguration configuration )
    {
        super( configuration );
    }

    @Override
    public String getTableName()
    {
        return "validationresult";
    }

    @Override
    public String getAutoIncrementColumn()
    {
        return "validationresultid";
    }

    @Override
    public boolean isInclusiveUniqueColumns()
    {
        return true;
    }

    @Override
    public List<String> getIdentifierColumns()
    {
        return getStringList( "validationresultid" );
    }

    @Override
    public List<Object> getIdentifierValues( ValidationResult validationResult )
    {
        return getObjectList( validationResult.getId() );
    }

    @Override
    public List<String> getUniqueColumns()
    {
        return getStringList(
            "validationruleid",
            "periodid",
            "organisationunitid",
            "attributeoptioncomboid",
            "dayinperiod"
        );
    }

    @Override
    public List<Object> getUniqueValues( ValidationResult validationResult )
    {
        return getObjectList(
            validationResult.getValidationRule().getId(),
            validationResult.getPeriod().getId(),
            validationResult.getOrganisationUnit().getId(),
            validationResult.getAttributeOptionCombo().getId(),
            validationResult.getDayInPeriod()
        );
    }

    @Override
    public List<String> getColumns()
    {
        return getStringList(
            "leftsidevalue",
            "rightsidevalue",
            "validationruleid",
            "periodid",
            "organisationunitid",
            "attributeoptioncomboid",
            "dayinperiod"
        );
    }

    @Override
    public List<Object> getValues( ValidationResult validationResult )
    {
        return getObjectList(
            validationResult.getLeftsideValue(),
            validationResult.getRightsideValue(),
            validationResult.getValidationRule().getId(),
            validationResult.getPeriod().getId(),
            validationResult.getOrganisationUnit().getId(),
            validationResult.getAttributeOptionCombo().getId(),
            validationResult.getDayInPeriod()
        );
    }

    @Override
    public ValidationResult mapRow( ResultSet resultSet )
        throws SQLException
    {
        return null;
    }
}
