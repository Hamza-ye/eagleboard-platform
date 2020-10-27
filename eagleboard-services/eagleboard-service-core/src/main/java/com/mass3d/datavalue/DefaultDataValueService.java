package com.mass3d.datavalue;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mass3d.external.conf.ConfigurationKey.CHANGELOG_AGGREGATE;
import static com.mass3d.system.util.ValidationUtils.dataValueIsValid;
import static com.mass3d.system.util.ValidationUtils.dataValueIsZeroAndInsignificant;

import com.mass3d.todotask.TodoTask;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import com.mass3d.common.AuditType;
import com.mass3d.common.IllegalQueryException;
import com.mass3d.dataelement.DataElement;
import com.mass3d.external.conf.DhisConfigurationProvider;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorMessage;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.user.CurrentUserService;
import com.mass3d.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data value service implementation. Note that data values are softly deleted,
 * which implies having the deleted property set to true and updated.
 *
 */
@Slf4j
@Service( "com.mass3d.datavalue.DataValueService" )
public class DefaultDataValueService
    implements DataValueService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final DataValueStore dataValueStore;

    private final DataValueAuditService dataValueAuditService;

    private final CurrentUserService currentUserService;

//    private final CategoryService categoryService;

    private final DhisConfigurationProvider config;

    public DefaultDataValueService( DataValueStore dataValueStore, DataValueAuditService dataValueAuditService,
        CurrentUserService currentUserService, DhisConfigurationProvider config )
    {
        checkNotNull( dataValueAuditService );
        checkNotNull( dataValueStore );
        checkNotNull( currentUserService );
//        checkNotNull( categoryService );
        checkNotNull( config );

        this.dataValueStore = dataValueStore;
        this.dataValueAuditService = dataValueAuditService;
        this.currentUserService = currentUserService;
//        this.categoryService = categoryService;
        this.config = config;
    }

    // -------------------------------------------------------------------------
    // Basic DataValue
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public boolean addDataValue( DataValue dataValue )
    {
        // ---------------------------------------------------------------------
        // Validation
        // ---------------------------------------------------------------------

        if ( dataValue == null || dataValue.isNullValue() )
        {
            log.info( "Data value is null" );
            return false;
        }

        String result = dataValueIsValid( dataValue.getValue(), dataValue.getDataElement() );

        if ( result != null )
        {
            log.info( "Data value is not valid: " + result );
            return false;
        }

        boolean zeroInsignificant = dataValueIsZeroAndInsignificant( dataValue.getValue(), dataValue.getDataElement() );

        if ( zeroInsignificant )
        {
            log.info( "Data value is zero and insignificant" );
            return false;
        }

        // ---------------------------------------------------------------------
        // Set default category option combo if null
        // ---------------------------------------------------------------------

//        if ( dataValue.getCategoryOptionCombo() == null )
//        {
//            dataValue.setCategoryOptionCombo( categoryService.getDefaultCategoryOptionCombo() );
//        }
//
//        if ( dataValue.getAttributeOptionCombo() == null )
//        {
//            dataValue.setAttributeOptionCombo( categoryService.getDefaultCategoryOptionCombo() );
//        }

        dataValue.setCreated( new Date() );
        dataValue.setLastUpdated( new Date() );

        // ---------------------------------------------------------------------
        // Check and restore soft deleted value
        // ---------------------------------------------------------------------

        DataValue softDelete = dataValueStore.getSoftDeletedDataValue( dataValue );

        if ( softDelete != null )
        {
            softDelete.mergeWith( dataValue );
            softDelete.setDeleted( false );

            dataValueStore.updateDataValue( softDelete );
        }
        else
        {
            dataValueStore.addDataValue( dataValue );
        }

        return true;
    }

    @Override
    @Transactional
    public void updateDataValue( DataValue dataValue )
    {
        if ( dataValue.isNullValue() ||
            dataValueIsZeroAndInsignificant( dataValue.getValue(), dataValue.getDataElement() ) )
        {
            deleteDataValue( dataValue );
        }
        else if ( dataValueIsValid( dataValue.getValue(), dataValue.getDataElement() ) == null )
        {
            dataValue.setLastUpdated( new Date() );

            DataValueAudit dataValueAudit = new DataValueAudit( dataValue, dataValue.getAuditValue(),
                dataValue.getStoredBy(), AuditType.UPDATE );

            if ( config.isEnabled( CHANGELOG_AGGREGATE ) )
            {
                dataValueAuditService.addDataValueAudit( dataValueAudit );
            }

            dataValueStore.updateDataValue( dataValue );
        }
    }

    @Override
    @Transactional
    public void updateDataValues( List<DataValue> dataValues )
    {
        if ( dataValues != null && !dataValues.isEmpty() )
        {
            for ( DataValue dataValue : dataValues )
            {
                updateDataValue( dataValue );
            }
        }
    }

    @Override
    @Transactional
    public void deleteDataValue( DataValue dataValue )
    {
        DataValueAudit dataValueAudit = new DataValueAudit( dataValue, dataValue.getAuditValue(),
            currentUserService.getCurrentUsername(), AuditType.DELETE );

        if ( config.isEnabled( CHANGELOG_AGGREGATE ) )
        {
            dataValueAuditService.addDataValueAudit( dataValueAudit );
        }

        dataValue.setLastUpdated( new Date() );
        dataValue.setDeleted( true );

        dataValueStore.updateDataValue( dataValue );
    }

    @Override
    @Transactional
    public void deleteDataValues( TodoTask todoTask )
    {
        dataValueStore.deleteDataValues( todoTask );
    }

    @Override
    @Transactional
    public void deleteDataValues( DataElement dataElement )
    {
        dataValueStore.deleteDataValues( dataElement );
    }

    @Override
    @Transactional(readOnly = true)
    public DataValue getDataValue( DataElement dataElement, Period period, TodoTask source )
    {
//        CategoryOptionCombo defaultOptionCombo = categoryService.getDefaultCategoryOptionCombo();

        return dataValueStore.getDataValue( dataElement, period, source );
    }

//    @Override
//    @Transactional(readOnly = true)
//    public DataValue getDataValue( DataElement dataElement, Period period, TodoTask source )
//    {
//        return dataValueStore.getDataValue( dataElement, period, source );
//    }

    // -------------------------------------------------------------------------
    // Collections of DataValues
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<DataValue> getDataValues( DataExportParams params )
    {
        validate( params );

        return dataValueStore.getDataValues( params );
    }

    @Override
    public void validate( DataExportParams params )
    {
        ErrorMessage error = null;

        if ( params == null )
        {
            throw new IllegalQueryException( ErrorCode.E2000 );
        }

        if ( !params.hasDataElements() && !params.hasDataSets() && !params.hasDataElementGroups() )
        {
            error = new ErrorMessage( ErrorCode.E2001 );
        }

        if ( !params.hasPeriods() && !params.hasStartEndDate() && !params.hasLastUpdated() && !params.hasLastUpdatedDuration() )
        {
            error = new ErrorMessage( ErrorCode.E2002 );
        }

        if ( params.hasPeriods() && params.hasStartEndDate() )
        {
            error = new ErrorMessage( ErrorCode.E2003 );
        }

        if ( params.hasStartEndDate() && params.getStartDate().after( params.getEndDate() ) )
        {
            error = new ErrorMessage( ErrorCode.E2004 );
        }

        if ( params.hasLastUpdatedDuration() && DateUtils.getDuration( params.getLastUpdatedDuration() ) == null )
        {
            error = new ErrorMessage( ErrorCode.E2005 );
        }

//        if ( !params.hasTodoTasks() && !params.hasOrganisationUnitGroups() )
//        {
//            error = new ErrorMessage( ErrorCode.E2006 );
//        }
//
//        if ( params.isIncludeChildren() && params.hasOrganisationUnitGroups() )
//        {
//            error = new ErrorMessage( ErrorCode.E2007 );
//        }

        if ( params.isIncludeChildren() && !params.hasTodoTasks() )
        {
            error = new ErrorMessage( ErrorCode.E2008 );
        }

        if ( params.hasLimit() && params.getLimit() < 0 )
        {
            error = new ErrorMessage( ErrorCode.E2009, params.getLimit() );
        }

        if ( error != null )
        {
            log.warn( "Validation failed: " + error );

            throw new IllegalQueryException( error );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataValue> getAllDataValues()
    {
        return dataValueStore.getAllDataValues();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataValue> getDataValues( TodoTask source, Period period,
        Collection<DataElement> dataElements )
    {
        return dataValueStore.getDataValues( source, period, dataElements );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeflatedDataValue> getDeflatedDataValues( DataExportParams params )
    {
        return dataValueStore.getDeflatedDataValues( params );
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataValueCount( int days )
    {
        Calendar cal = PeriodType.createCalendarInstance();
        cal.add( Calendar.DAY_OF_YEAR, (days * -1) );

        return dataValueStore.getDataValueCountLastUpdatedBetween( cal.getTime(), null, false );
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataValueCountLastUpdatedAfter( Date date, boolean includeDeleted )
    {
        return dataValueStore.getDataValueCountLastUpdatedBetween( date, null, includeDeleted );
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataValueCountLastUpdatedBetween( Date startDate, Date endDate, boolean includeDeleted )
    {
        return dataValueStore.getDataValueCountLastUpdatedBetween( startDate, endDate, includeDeleted );
    }
}
