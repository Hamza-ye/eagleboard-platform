package com.mass3d.program;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.dataentryform.DataEntryForm;

public interface ProgramStageStore
    extends IdentifiableObjectStore<ProgramStage>
{

    /**
     * Retrieve a program stage by name and a program
     *
     * @param name    Name of program stage
     * @param program Specify a {@link Program} for retrieving a program stage.
     *                The system allows the name of program stages are duplicated on
     *                different programs
     * @return ProgramStage
     */
    ProgramStage getByNameAndProgram(String name, Program program);

    /**
     * Get all ProgramStages associated with the given DataEntryForm.
     * @param dataEntryForm the DataEntryForm.
     * @return a list of ProgramStages.
     */
    List<ProgramStage> getByDataEntryForm(DataEntryForm dataEntryForm);
}
