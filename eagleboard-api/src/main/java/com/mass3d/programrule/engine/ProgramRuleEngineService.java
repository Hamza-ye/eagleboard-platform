package com.mass3d.programrule.engine;

import java.util.List;
import org.hisp.dhis.rules.models.RuleEffect;
import org.hisp.dhis.rules.models.RuleValidationResult;

public interface ProgramRuleEngineService
{
    /**
     * Call rule engine to evaluate the target enrollment and get a list of rule
     * effects, then run the actions present in these effects
     *
     * @param enrollment Uid of the target enrollment
     * @return the list of rule effects calculated by rule engine
     */
    List<RuleEffect> evaluateEnrollmentAndRunEffects(long enrollment);

    /**
     * Call rule engine to evaluate the target event and get a list of rule effects,
     * then run the actions present in these effects
     *
     * @param event Uid of the target event
     * @return the list of rule effects calculated by rule engine
     */
    List<RuleEffect> evaluateEventAndRunEffects(long event);

    RuleValidationResult getDescription(String condition, String programId);
}
