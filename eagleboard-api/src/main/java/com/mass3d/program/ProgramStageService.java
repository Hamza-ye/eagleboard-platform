package com.mass3d.program;

import java.util.List;
import com.mass3d.dataentryform.DataEntryForm;

/**
 * @version $Id$
 */
public interface ProgramStageService
{
    String ID = ProgramStageService.class.getName();

    // -------------------------------------------------------------------------
    // ProgramStage
    // -------------------------------------------------------------------------

    /**
     * Adds an {@link ProgramStage}
     *
     * @param programStage The to ProgramStage add.
     * @return A generated unique id of the added {@link ProgramStage}.
     */
    long saveProgramStage(ProgramStage programStage);

    /**
     * Deletes a {@link ProgramStage}.
     *
     * @param programStage the ProgramStage to delete.
     */
    void deleteProgramStage(ProgramStage programStage);

    /**
     * Updates an {@link ProgramStage}.
     *
     * @param programStage the ProgramStage to update.
     */
    void updateProgramStage(ProgramStage programStage);

    /**
     * Returns a {@link ProgramStage}.
     *
     * @param id the id of the ProgramStage to return.
     * @return the ProgramStage with the given id
     */
    ProgramStage getProgramStage(long id);

    /**
     * Returns the {@link ProgramStage} with the given UID.
     *
     * @param uid the UID.
     * @return the ProgramStage with the given UID, or null if no match.
     */
    ProgramStage getProgramStage(String uid);

    /**
     * Retrieve all ProgramStages associated with the given DataEntryForm.
     * @param dataEntryForm the DataEntryForm.
     * @return a list og ProgramStages.
     */
    List<ProgramStage> getProgramStagesByDataEntryForm(DataEntryForm dataEntryForm);
}
