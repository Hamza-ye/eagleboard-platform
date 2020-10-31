package com.mass3d.program;

public interface ProgramOwnershipHistoryStore
{

    String ID = ProgramOwnershipHistoryStore.class.getName();

    /**
     * Adds program ownership history record
     * 
     * @param programOwnershipHistory the ownership history to add
     */
    void addProgramOwnershipHistory(ProgramOwnershipHistory programOwnershipHistory);

}
