package com.mass3d.programrule;

import java.util.List;
import java.util.Set;
import com.mass3d.program.Program;

public interface ProgramRuleService
{
    /**
     * Adds an {@link ProgramRule}
     *
     * @param programRule The to ProgramRule add.
     * @return A generated unique id of the added {@link ProgramRule}.
     */
    long addProgramRule(ProgramRule programRule);

    /**
     * Deletes a {@link ProgramRule}
     *
     * @param programRule The ProgramRule to delete.
     */
    void deleteProgramRule(ProgramRule programRule);

    /**
     * Updates an {@link ProgramRule}.
     *
     * @param programRule The ProgramRule to update.
     */
    void updateProgramRule(ProgramRule programRule);

    /**
     * Returns a {@link ProgramRule}.
     *
     * @param id the id of the ProgramRule to return.
     * @return the ProgramRule with the given id
     */
    ProgramRule getProgramRule(long id);

    /**
     * Returns a {@link ProgramRule}.
     *
     * @param uid the uid of the ProgramRule to return.
     * @return the ProgramRule with the given uid
     */
    ProgramRule getProgramRule(String uid);

    /**
     * Returns a {@link ProgramRule}.
     *
     * @param name the name of the ProgramRule to return.
     * @param program {@link Program}.
     * @return the ProgramRule with the given name
     */
    ProgramRule getProgramRuleByName(String name, Program program);

    /**
     * Returns all {@link ProgramRule}.
     *
     * @return a collection of all ProgramRule, or an empty collection if
     * there are no ProgramRules.
     */
    List<ProgramRule> getAllProgramRule();

    List<ProgramRule> getImplementableProgramRules(Program program,
        Set<ProgramRuleActionType> types);

    /**
     * Get validation by {@link Program}
     *
     * @param program Program
     * @return ProgramRule list
     */
    List<ProgramRule> getProgramRule(Program program);

    /**
     * Get validation by {@link Program}
     *
     * @param program Program
     * @param key Search Program Rule by key
     * @return ProgramRule list
     */
    List<ProgramRule> getProgramRules(Program program, String key);

    /**
     *
     * @return all {@link ProgramRule} with no priority
     */
    List<ProgramRule> getProgramRulesWithNoPriority();

    List<ProgramRule> getProgramRulesWithNoCondition();

    List<ProgramRule> getProgramRulesByEvaluationTime(
        ProgramRuleActionEvaluationTime evaluationTime);

    List<ProgramRule> getProgramRulesByEvaluationEnvironment(
        ProgramRuleActionEvaluationEnvironment evaluationEnvironment);

    List<ProgramRule> getProgramRulesWithNoAction();
}
