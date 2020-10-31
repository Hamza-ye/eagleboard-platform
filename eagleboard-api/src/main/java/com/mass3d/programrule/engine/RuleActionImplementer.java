package com.mass3d.programrule.engine;

import com.mass3d.program.ProgramInstance;
import com.mass3d.program.ProgramStageInstance;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleEffect;

/**
 * Service is responsible for implementing actions which are generated as a result of Rule-Engine evaluations.
 * Each action type has a corresponding RuleActionImplementer class responsible for carrying out the action.
 *
 */
public interface RuleActionImplementer
{
    boolean accept(RuleAction ruleAction);

    void implement(RuleEffect ruleEffect, ProgramInstance programInstance);

    void implement(RuleEffect ruleEffect, ProgramStageInstance programStageInstance);

    /**
     * This method is directly called by SideEffectHandlerService to implement actions
     *
     * @param ruleEffect received tracker importer
     * @param programInstance enrollment to implement the action against
     */
    void implementEnrollmentAction(RuleEffect ruleEffect, String programInstance);

    /**
     * This method is directly called by SideEffectHandlerService to implement actions
     *
     * @param ruleEffect received tracker importer
     * @param programStageInstance event to implement the action against
     */
    void implementEventAction(RuleEffect ruleEffect, String programStageInstance);
}
