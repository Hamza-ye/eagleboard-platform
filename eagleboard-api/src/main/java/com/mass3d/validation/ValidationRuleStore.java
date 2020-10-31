package com.mass3d.validation;

import java.util.List;
import com.mass3d.common.IdentifiableObjectStore;

public interface ValidationRuleStore
    extends IdentifiableObjectStore<ValidationRule>
{
    String ID = ValidationRuleStore.class.getName();

    /**
     * Returns all ValidationRules that should be used for form validation.
     * 
     * @return a List of ValidationRules.
     */
    List<ValidationRule> getAllFormValidationRules();

    /**
     * Returns all ValidationRules which have associated ValidationNotificationTemplates.
     *
     * @return a List of ValidationRules.
     */
    List<ValidationRule> getValidationRulesWithNotificationTemplates();
}
