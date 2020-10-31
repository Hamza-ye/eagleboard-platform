package com.mass3d.programrule;

import java.util.List;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.Program;

public interface ProgramRuleVariableService
{
    /**
     * Adds an {@link ProgramRuleVariable}
     *
     * @param programRuleVariable The to ProgramRuleVariable add.
     * @return A generated unique id of the added {@link ProgramRuleVariable}.
     */
    long addProgramRuleVariable(ProgramRuleVariable programRuleVariable);

    /**
     * Deletes a {@link ProgramRuleVariable}
     *
     * @param programRuleVariable The ProgramRuleVariable to delete.
     */
    void deleteProgramRuleVariable(ProgramRuleVariable programRuleVariable);

    /**
     * Updates an {@link ProgramRuleVariable}.
     *
     * @param programRuleVariable The ProgramRuleVariable to update.
     */
    void updateProgramRuleVariable(ProgramRuleVariable programRuleVariable);

    /**
     * Returns a {@link ProgramRuleVariable}.
     *
     * @param id the id of the ProgramRuleVariable to return.
     * @return the ProgramRuleVariable with the given id
     */
    ProgramRuleVariable getProgramRuleVariable(long id);

    /**
     * Returns all {@link ProgramRuleVariable}.
     *
     * @return a List of all ProgramRuleVariable, or an empty List if
     * there are no ProgramRuleVariables.
     */
    List<ProgramRuleVariable> getAllProgramRuleVariable();

    /**
     * Get validation by {@link Program}
     *
     * @param program Program
     * @return ProgramRuleVariable list
     */
    List<ProgramRuleVariable> getProgramRuleVariable(Program program);

    /**
     *
     * @param program program.
     * @param dataElement to find association with.
     * @return true if dataElement is associated with any ProgramRuleVariable, false otherwise.
     */
    boolean isLinkedToProgramRuleVariable(Program program, DataElement dataElement);

    /**
     *
     * @return all ProgramRuleVariables which are linked to {@link DataElement}.
     */
    List<ProgramRuleVariable> getVariablesWithNoDataElement();

    /**
     *
     * @return all ProgramRuleVariables which are linked to {@link com.mass3d.trackedentity.TrackedEntityAttribute}
     */
    List<ProgramRuleVariable> getVariablesWithNoAttribute();
}
