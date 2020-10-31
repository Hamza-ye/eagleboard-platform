package com.mass3d.program;

public interface ProgramOwnershipHistoryService
{

    String ID = ProgramOwnershipHistoryService.class.getName();

    /**
     * Adds program ownership history
     * 
     * @param programOwnershipHistory the history to add
     */
    void addProgramOwnershipHistory(ProgramOwnershipHistory programOwnershipHistory);
}
