package com.mass3d.programrule;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.dataelement.DataElement;
import com.mass3d.program.Program;

public interface ProgramRuleVariableStore
    extends IdentifiableObjectStore<ProgramRuleVariable>
{
    /**
     * Get programRuleVariable by program
     *
     * @param program {@link Program}
     * @return ProgramRuleVariable list
     */
    List<ProgramRuleVariable> get(Program program);


    /**
     * @param program program
     * @param dataElement to find association with
     * @return list of ProgramRuleVariables associated with given dataElement
     */
    List<ProgramRuleVariable> getProgramVariables(Program program, DataElement dataElement);

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
