package com.mass3d.datavalue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mass3d.todotask.TodoTask;
import java.util.List;
import com.mass3d.common.AuditType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.period.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Quang Nguyen
 * @author Halvdan Hoem Grelland
 */
@Service( "com.mass3d.datavalue.DataValueAuditService" )
public class DefaultDataValueAuditService
    implements DataValueAuditService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final DataValueAuditStore dataValueAuditStore;

    public DefaultDataValueAuditService( DataValueAuditStore dataValueAuditStore )
    {
        checkNotNull( dataValueAuditStore );

        this.dataValueAuditStore = dataValueAuditStore;
    }

    // -------------------------------------------------------------------------
    // DataValueAuditService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void addDataValueAudit( DataValueAudit dataValueAudit )
    {
        dataValueAuditStore.addDataValueAudit( dataValueAudit );
    }
    
    @Override
    @Transactional
    public void deleteDataValueAudits( TodoTask organisationUnit )
    {
        dataValueAuditStore.deleteDataValueAudits( organisationUnit );
    }

    @Override
    @Transactional
    public void deleteDataValueAudits( DataElement dataElement )
    {
        dataValueAuditStore.deleteDataValueAudits( dataElement );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataValueAudit> getDataValueAudits( DataValue dataValue )
    {
        return dataValueAuditStore.getDataValueAudits( dataValue );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataValueAudit> getDataValueAudits( List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks,
         AuditType auditType )
    {
        return dataValueAuditStore.getDataValueAudits( dataElements, periods, todoTasks, auditType );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataValueAudit> getDataValueAudits( List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks,
         AuditType auditType, int first, int max )
    {
        return dataValueAuditStore.getDataValueAudits( dataElements, periods, todoTasks, auditType, first, max );
    }

    @Override
    @Transactional(readOnly = true)
    public int countDataValueAudits( List<DataElement> dataElements, List<Period> periods, List<TodoTask> todoTasks,
         AuditType auditType )
    {
        return dataValueAuditStore.countDataValueAudits( dataElements, periods, todoTasks, auditType );
    }
}
