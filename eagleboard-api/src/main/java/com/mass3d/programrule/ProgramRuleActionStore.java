package com.mass3d.programrule;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface ProgramRuleActionStore
    extends IdentifiableObjectStore<ProgramRuleAction>
{
    /**
     * Get programRuleAction by program
     *
     * @param programRule {@link ProgramRule}
     * @return ProgramRuleActionVariable list
     */
    List<ProgramRuleAction> get(ProgramRule programRule);

    List<ProgramRuleAction> getProgramActionsWithNoDataObject();

    List<ProgramRuleAction> getProgramActionsWithNoNotification();

    List<ProgramRuleAction> getMalFormedRuleActionsByType(ProgramRuleActionType type);
}
