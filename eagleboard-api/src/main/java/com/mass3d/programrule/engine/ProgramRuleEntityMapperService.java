package com.mass3d.programrule.engine;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import com.mass3d.programrule.ProgramRule;
import com.mass3d.programrule.ProgramRuleVariable;
import org.hisp.dhis.rules.DataItem;
import org.hisp.dhis.rules.models.*;

/**
 * RuleEngine has its own domain model. This service is responsible for
 * converting DHIS domain objects to RuleEngine domain objects and vice versa.
 *
 */
public interface ProgramRuleEntityMapperService
{
    /***
     * @return A list of mapped Rules for all programs
     */
    List<Rule> toMappedProgramRules();

    /**
     * @param programRules The list of program rules to be mapped
     * @return A list of mapped Rules for list of programs.
     */
    List<Rule> toMappedProgramRules(List<ProgramRule> programRules);

    /***
     * @return A list of mapped RuleVariables for all programs.
     */
    List<RuleVariable> toMappedProgramRuleVariables();

    /**
     * @param programRuleVariables The list of ProgramRuleVariable to be mapped.
     * @return A list of mapped RuleVariables for list of programs.
     */
    List<RuleVariable> toMappedProgramRuleVariables(List<ProgramRuleVariable> programRuleVariables);

    /**
     * @param programStageInstances list of events
     * @param psiToEvaluate event to filter out from the resulting list.
     *
     * @return A list of mapped events for the list of DHIS events.
     */
    List<RuleEvent> toMappedRuleEvents(Set<ProgramStageInstance> programStageInstances,
        ProgramStageInstance psiToEvaluate);

    /**
     * @param psiToEvaluate event to converted.
     * @return A mapped event for corresponding DHIS event.
     */
    RuleEvent toMappedRuleEvent(ProgramStageInstance psiToEvaluate);

    /**
     * @return A mapped RuleEnrollment for DHIS enrollment i.e ProgramInstance.
     */
    RuleEnrollment toMappedRuleEnrollment(ProgramInstance programInstance);

    /**
     * Fetch display name for {@link ProgramRuleVariable}, {@link com.mass3d.constant.Constant}
     * @return map containing item description
     */
    Map<String, DataItem> getItemStore(List<ProgramRuleVariable> programRuleVariables);
}
