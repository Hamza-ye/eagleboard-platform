package com.mass3d.program;

import java.util.List;

public interface ProgramTempOwnershipAuditService
{

    String ID = ProgramTempOwnershipAuditService.class.getName();

    /**
     * Adds program temp ownership audit
     * 
     * @param programTempOwnershipAudit the audit to add
     */
    void addProgramTempOwnershipAudit(ProgramTempOwnershipAudit programTempOwnershipAudit);

    /**
     * Deletes program temp ownership audit for the given program instance
     *
     * @param program the program
     */
    void deleteProgramTempOwnershipAudit(Program program);

    /**
     * Returns program temp ownership audits matching query params
     *
     * @param params program temp ownership audit query params
     * @return matching ProgramTempOwnershipAuditQueryParams
     */
    List<ProgramTempOwnershipAudit> getProgramTempOwnershipAudits(
        ProgramTempOwnershipAuditQueryParams params);

    /**
     * Returns count of program temp ownership audits matching query params
     *
     * @param params program temp ownership audit query params
     * @return count of ProgramTempOwnershipAuditQueryParams
     */
    int getProgramTempOwnershipAuditsCount(ProgramTempOwnershipAuditQueryParams params);

}
