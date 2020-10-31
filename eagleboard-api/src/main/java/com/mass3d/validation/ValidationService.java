package com.mass3d.validation;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataset.DataSet;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.period.Period;

public interface ValidationService
{
    int MAX_INTERACTIVE_ALERTS = 500;
    int MAX_SCHEDULED_ALERTS = 100000;

    /**
     * Start a validation analysis, based on the supplied parameters. See ValidationAnalysisParams for more
     * information
     *
     * @param parameters the parameters to base the analysis on.
     * @return a collection of ValidationResults found.
     */
    Collection<ValidationResult> validationAnalysis(ValidationAnalysisParams parameters);

    /**
     * Validate that missing data values have a corresponding comment, assuming
     * that the given data set has the noValueRequiresComment property set to true.
     *
     * @param dataSet              the data set.
     * @param period               the period.
     * @param orgUnit              the organisation unit.
     * @param attributeOptionCombo the attribute option combo.
     * @return a list of operands representing missing comments.
     */
    List<DataElementOperand> validateRequiredComments(DataSet dataSet, Period period,
        OrganisationUnit orgUnit,
        CategoryOptionCombo attributeOptionCombo);

    ValidationAnalysisParams.Builder newParamsBuilder(Collection<ValidationRule> validationRules,
        OrganisationUnit organisationUnit, Collection<Period> periods);

    ValidationAnalysisParams.Builder newParamsBuilder(ValidationRuleGroup validationRuleGroup,
        OrganisationUnit organisationUnit, Date startDate, Date endDate);

    ValidationAnalysisParams.Builder newParamsBuilder(DataSet dataSet,
        OrganisationUnit organisationUnits,
        Period period);

}
