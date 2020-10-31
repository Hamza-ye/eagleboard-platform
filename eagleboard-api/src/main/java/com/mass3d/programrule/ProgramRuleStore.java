package com.mass3d.programrule;

import java.util.List;
import java.util.Set;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.program.Program;

public interface ProgramRuleStore
    extends IdentifiableObjectStore<ProgramRule>
{
    /**
     * Get programRule by program
     *
     * @param program {@link Program}
     * @return ProgramRuleVariable list
     */
    List<ProgramRule> get(Program program);

    /**
     * Returns a {@link ProgramRule}.
     *
     * @param name the name of the ProgramRule to return.
     * @param program {@link Program}.
     * @return the ProgramRule with the given name
     */
    ProgramRule getByName(String name, Program program);

    /**
     * Get validation by {@link Program}
     *
     * @param program Program
     * @param key Search Program Rule by key
     * @return ProgramRule list
     */
    List<ProgramRule> get(Program program, String key);

    List<ProgramRule> getImplementableProgramRules(Program program,
        Set<ProgramRuleActionType> types);

    List<ProgramRule> getProgramRulesWithNoCondition();

    List<ProgramRule> getProgramRulesWithNoPriority();

    List<ProgramRule> getProgramRulesByEvaluationTime(
        ProgramRuleActionEvaluationTime evaluationTime);

    List<ProgramRule> getProgramRulesByEvaluationEnvironment(
        ProgramRuleActionEvaluationEnvironment environment);

    List<ProgramRule> getProgramRulesWithNoAction();
}
