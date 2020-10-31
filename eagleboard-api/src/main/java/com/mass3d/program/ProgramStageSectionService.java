package com.mass3d.program;

/**
 * @version ProgramStageSectionService.java 11:12:41 AM Aug 22, 2012 $
 */
public interface ProgramStageSectionService
{
    String ID = ProgramStageSection.class.getName();

    // -------------------------------------------------------------------------
    // ProgramStageSection
    // -------------------------------------------------------------------------

    /**
     * Adds an {@link ProgramStageSection}
     *
     * @param programStageSection The to ProgramStageSection add.
     * @return A generated unique id of the added {@link ProgramStageSection}.
     */
    long saveProgramStageSection(ProgramStageSection programStageSection);

    /**
     * Deletes a {@link ProgramStageSection}.
     *
     * @param programStageSection the ProgramStageSection to delete.
     */
    void deleteProgramStageSection(ProgramStageSection programStageSection);

    /**
     * Updates an {@link ProgramStageSection}.
     *
     * @param programStageSection the ProgramStageSection to update.
     */
    void updateProgramStageSection(ProgramStageSection programStageSection);

    /**
     * Returns a {@link ProgramStageSection}.
     *
     * @param id the id of the ProgramStageSection to return.
     * @return the ProgramStageSection with the given id
     */
    ProgramStageSection getProgramStageSection(long id);
}
