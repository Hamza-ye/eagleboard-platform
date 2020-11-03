package com.mass3d.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( "com.mass3d.program.ProgramOwnershipHistoryService" )
@Transactional
public class DefaultProgramOwnershipHistoryService implements ProgramOwnershipHistoryService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    @Autowired
    private ProgramOwnershipHistoryStore programOwnershipHistoryStore;

    // -------------------------------------------------------------------------
    // ProgramOwnershipHistoryService implementation
    // -------------------------------------------------------------------------

    @Override
    public void addProgramOwnershipHistory( ProgramOwnershipHistory programOwnershipHistory )
    {
        programOwnershipHistoryStore.addProgramOwnershipHistory( programOwnershipHistory );
    }

}