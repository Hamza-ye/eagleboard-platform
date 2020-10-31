package com.mass3d.validation;

import java.util.Collection;
import java.util.List;
import com.mass3d.common.GenericStore;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.period.Period;
import com.mass3d.validation.comparator.ValidationResultQuery;

public interface ValidationResultStore
    extends GenericStore<ValidationResult>
{
    List<ValidationResult> getAllUnreportedValidationResults();

    ValidationResult getById(long id);

    List<ValidationResult> query(ValidationResultQuery query);

    int count(ValidationResultQuery query);

    List<ValidationResult> getValidationResults(OrganisationUnit orgUnit,
        boolean includeOrgUnitDescendants, Collection<ValidationRule> validationRules,
        Collection<Period> periods);
}
