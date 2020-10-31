package com.mass3d.validation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.period.Period;
import com.mass3d.validation.comparator.ValidationResultQuery;

public interface ValidationResultService
{
    /**
     * Saves a set of ValidationResults in a bulk action.
     * 
     * @param validationResults a collection of validation results.
     */
    void saveValidationResults(Collection<ValidationResult> validationResults);

    /**
     * Returns a list of all existing ValidationResults.
     *
     * @return a list of validation results.
     */
    List<ValidationResult> getAllValidationResults();

    /**
     * Returns a list of all ValidationResults where notificationSent is false
     * @return a list of validation results.
     */
    List<ValidationResult> getAllUnReportedValidationResults();

    /**
     * Deletes the validationResult.
     *
     * @param validationResult the validation result.
     */
    void deleteValidationResult(ValidationResult validationResult);

    /**
     * Updates a list of ValidationResults.
     *
     * @param validationResults validationResults to update.
     */
    void updateValidationResults(Set<ValidationResult> validationResults);

    /**
     * Returns the ValidationResult with the given id, or null if no validation result exists with that id.
     *
     * @param id the validation result identifier.
     * @return a validation result.
     */
    ValidationResult getById(long id);

    List<ValidationResult> getValidationResults(ValidationResultQuery query);

    int countValidationResults(ValidationResultQuery query);

    List<ValidationResult> getValidationResults(OrganisationUnit orgUnit,
        boolean includeOrgUnitDescendants, Collection<ValidationRule> validationRules,
        Collection<Period> periods);
}
