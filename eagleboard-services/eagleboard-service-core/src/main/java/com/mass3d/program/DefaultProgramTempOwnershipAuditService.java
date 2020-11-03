package com.mass3d.program;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramTempOwnershipAuditService" )
public class DefaultProgramTempOwnershipAuditService implements ProgramTempOwnershipAuditService
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private ProgramTempOwnershipAuditStore programTempOwnershipAuditStore;

    // -------------------------------------------------------------------------
    // ProgramTempOwnershipAuditService implementation
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void addProgramTempOwnershipAudit( ProgramTempOwnershipAudit programTempOwnershipAudit )
    {
        programTempOwnershipAuditStore.addProgramTempOwnershipAudit( programTempOwnershipAudit );
    }

    @Override
    @Transactional
    public void deleteProgramTempOwnershipAudit( Program program )
    {
        programTempOwnershipAuditStore.deleteProgramTempOwnershipAudit( program );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramTempOwnershipAudit> getProgramTempOwnershipAudits( ProgramTempOwnershipAuditQueryParams params )
    {
        return programTempOwnershipAuditStore.getProgramTempOwnershipAudits( params );
    }

    @Override
    @Transactional(readOnly = true)
    public int getProgramTempOwnershipAuditsCount( ProgramTempOwnershipAuditQueryParams params )
    {
        return programTempOwnershipAuditStore.getProgramTempOwnershipAuditsCount( params );
    }
}