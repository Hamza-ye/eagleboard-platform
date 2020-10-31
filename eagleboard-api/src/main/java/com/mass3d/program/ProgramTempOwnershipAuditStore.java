package com.mass3d.program;

import java.util.List;

public interface ProgramTempOwnershipAuditStore
{

    String ID = ProgramTempOwnershipAuditStore.class.getName();

    /**
     * Adds program temp ownership audit
     * 
     * @param programTempOwnershipAudit the audit to add
     */
    void addProgramTempOwnershipAudit(ProgramTempOwnershipAudit programTempOwnershipAudit);

    /**
     * Deletes  audit for the given program
     *
     * @param program the program instance
     */
    void deleteProgramTempOwnershipAudit(Program program);

    /**
     * Returns program temp ownership audits matching query params
     *
     * @param params program temp ownership audit query params
     * @return matching ProgramTempOwnershipAudit
     */
    List<ProgramTempOwnershipAudit> getProgramTempOwnershipAudits(
        ProgramTempOwnershipAuditQueryParams params);

    /**
     * Returns count of program temp ownership audits matching query params
     *
     * @param params program temp ownership audit query params
     * @return count of ProgramTempOwnershipAudit
     */
    int getProgramTempOwnershipAuditsCount(ProgramTempOwnershipAuditQueryParams params);

}
