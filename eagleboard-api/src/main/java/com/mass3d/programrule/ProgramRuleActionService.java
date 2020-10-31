package com.mass3d.programrule;

import java.util.List;

public interface ProgramRuleActionService
{
    /**
     * Adds a {@link ProgramRuleAction}.
     *
     * @param programRuleAction The to ProgramRuleAction add.
     * @return A generated unique id of the added {@link ProgramRuleAction}.
     */
    long addProgramRuleAction(ProgramRuleAction programRuleAction);

    /**
     * Deletes a {@link ProgramRuleAction}
     *
     * @param programRuleAction The ProgramRuleAction to delete.
     */
    void deleteProgramRuleAction(ProgramRuleAction programRuleAction);

    /**
     * Updates an {@link ProgramRuleAction}.
     *
     * @param programRuleAction The ProgramRuleAction to update.
     */
    void updateProgramRuleAction(ProgramRuleAction programRuleAction);

    /**
     * Returns a {@link ProgramRuleAction}.
     *
     * @param id the id of the ProgramRuleAction to return.
     * @return the ProgramRuleAction with the given id
     */
    ProgramRuleAction getProgramRuleAction(long id);

    /**
     * Returns all {@link ProgramRuleAction}.
     *
     * @return a collection of all ProgramRuleAction, or an empty collection if
     *          there are no ProgramRuleActions.
     */
    List<ProgramRuleAction> getAllProgramRuleAction();

    /**
     * Get validation by {@link ProgramRule}.
     *
     * @param programRule the program rule.
     * @return a list of ProgramRuleActions.
     */
    List<ProgramRuleAction> getProgramRuleAction(ProgramRule programRule);

    List<ProgramRuleAction> getProgramActionsWithNoLinkToDataObject();

    List<ProgramRuleAction> getProgramActionsWithNoLinkToNotification();

    List<ProgramRuleAction> getProgramRuleActionsWithNoSectionId();

    List<ProgramRuleAction> getProgramRuleActionsWithNoStageId();
}
