package com.mass3d.expression;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mass3d.common.DimensionalItemId;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.MapMap;
import com.mass3d.constant.Constant;
import com.mass3d.dataelement.DataElement;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorValue;
import com.mass3d.period.Period;

/**
 * Expressions are mathematical formulas and can contain references to various
 * elements.
 *
 */
public interface ExpressionService
{
    String ID = ExpressionService.class.getName();

    String DAYS_DESCRIPTION = "[Number of days]";
    String SYMBOL_DAYS = "[days]";
    String SYMBOL_WILDCARD = "*";

    String UID_EXPRESSION = "[a-zA-Z]\\w{10}";
    String INT_EXPRESSION = "^(0|-?[1-9]\\d*)$";

    // -------------------------------------------------------------------------
    // Expression CRUD operations
    // -------------------------------------------------------------------------

    /**
     * Adds a new Expression to the database.
     *
     * @param expression The Expression to add.
     * @return The generated identifier for this Expression.
     */
    long addExpression(Expression expression);

    /**
     * Updates an Expression.
     *
     * @param expression The Expression to update.
     */
    void updateExpression(Expression expression);

    /**
     * Deletes an Expression from the database.
     *
     * @param expression the expression.
     */
    void deleteExpression(Expression expression);

    /**
     * Get the Expression with the given identifier.
     *
     * @param id The identifier.
     * @return an Expression with the given identifier.
     */
    Expression getExpression(long id);

    /**
     * Gets all Expressions.
     *
     * @return A list with all Expressions.
     */
    List<Expression> getAllExpressions();

    // -------------------------------------------------------------------------
    // Indicator expression logic
    // -------------------------------------------------------------------------

    /**
     * Returns all dimensional item objects which are present in numerator and
     * denominator of the given indicators.
     *
     * @param indicators the collection of indicators.
     * @return a set of dimensional item objects.
     */
    Set<DimensionalItemObject> getIndicatorDimensionalItemObjects(Collection<Indicator> indicators);

//    /**
//     * Returns all OrganisationUnitGroups in the numerator and denominator
//     * expressions in the given Indicators. Returns an empty set if the given
//     * indicators are null or empty.
//     *
//     * @param indicators the set of indicators.
//     * @return a Set of OrganisationUnitGroups.
//     */
//    Set<OrganisationUnitGroup> getIndicatorOrgUnitGroups(Collection<Indicator> indicators);

    /**
     * Generates the calculated value for the given parameters based on the
     * values in the given maps.
     *
     * @param indicator the indicator for which to calculate the value.
     * @param periods a List of periods for which to calculate the value.
     * @param valueMap the map of data values.
     * @param constantMap the map of constants.
     * @param orgUnitCountMap the map of organisation unit group member counts.
     * @return the calculated value as a double.
     */
    IndicatorValue getIndicatorValueObject(Indicator indicator, List<Period> periods,
        Map<DimensionalItemObject, Double> valueMap, Map<String, Constant> constantMap,
        Map<String, Integer> orgUnitCountMap);

    /**
     * Substitutes any constants and org unit group member counts in the
     * numerator and denominator on all indicators in the given collection.
     *
     * @param indicators the set of indicators.
     */
    void substituteIndicatorExpressions(Collection<Indicator> indicators);

    // -------------------------------------------------------------------------
    // Expression logic
    // -------------------------------------------------------------------------

    /**
     * Tests whether the expression is valid.
     *
     * @param expression the expression formula.
     * @param parseType the type of expression to parse.
     * @return the ExpressionValidationOutcome of the validation.
     */
    ExpressionValidationOutcome expressionIsValid(String expression, ParseType parseType);

    /**
     * Creates an expression description containing the names of the
     * DimensionalItemObjects from an expression string.
     *
     * @param expression The expression string.
     * @param parseType the type of expression to parse.
     * @return An description containing DimensionalItemObjects names.
     */
    String getExpressionDescription(String expression, ParseType parseType);

    /**
     * Returns UIDs of Data Elements and associated Option Combos (if any)
     * found in the Data Element Operands an expression.
     * <p/>
     * If the Data Element Operand consists of just a Data Element, or if
     * the Option Combo is a wildcard "*", returns just dataElementUID.
     * <p/>
     * If an Option Combo is present, returns dataElementUID.optionComboUID.
     *
     * @param expression the expression.
     * @param parseType the type of expression to parse.
     * @return a Set of data element identifiers.
     */
    Set<String> getExpressionElementAndOptionComboIds(String expression, ParseType parseType);

    /**
     * Returns all data elements found in the given expression string,
     * including those found in data element operands.
     * Returns an empty set if the given expression is null.
     *
     * @param expression the expression string.
     * @param parseType the type of expression to parse.
     * @return a Set of data elements included in the expression string.
     */
    Set<DataElement> getExpressionDataElements(String expression, ParseType parseType);

//    /**
//     * Returns, in data element operand format, all data elements and all
//     * data element operands found in an expression. Returns an empty set
//     * if the expression is null.
//     *
//     * @param expression The expression string.
//     * @param parseType the type of expression to parse.
//     * @return A Set of Operands.
//     */
//    Set<DataElementOperand> getExpressionOperands(String expression, ParseType parseType);

    /**
     * Returns all CategoryOptionCombo uids in the given expression string
     * that are used as a data element operand categoryOptionCombo or
     * attributeOptionCombo. Returns an empty set if the expression is null.
     *
     * @param expression the expression string.
     * @param parseType the type of expression to parse.
     * @return a Set of CategoryOptionCombo uids in the expression string.
     */
    Set<String> getExpressionOptionComboIds(String expression, ParseType parseType);

    /**
     * Returns all dimensional item objects in the given expression.
     *
     * @param expression the expression to parse.
     * @param parseType the type of expression to parse.
     * @return a Set of dimensional item objects.
     */
    Set<DimensionalItemObject> getExpressionDimensionalItemObjects(String expression,
        ParseType parseType);

    /**
     * Returns all dimensional item objects in the given expression,
     * returning separately the items to be sampled inside any vector functions.
     *
     * @param expression the expression to parse.
     * @param parseType the type of expression to parse.
     * @param items Set to insert the items into.
     * @param sampleItems Set to insert the sampled items into.
     */
    void getExpressionDimensionalItemObjects(String expression, ParseType parseType,
        Set<DimensionalItemObject> items,
        Set<DimensionalItemObject> sampleItems);

    /**
     * Returns all dimensional item object ids in the given expression.
     *
     * @param expression the expression to parse
     * @param parseType the type of expression to parse.
     * @return a Set of dimensional item object ids.
     */
    Set<DimensionalItemId> getExpressionDimensionalItemIds(String expression, ParseType parseType);

//    /**
//     * Returns all OrganisationUnitGroups in the given expression.
//     *
//     * @param expression the expression string.
//     * @param parseType the type of expression to parse.
//     * @return a Set of OrganisationUnitGroups in the expression string.
//     */
//    Set<OrganisationUnitGroup> getExpressionOrgUnitGroups(String expression, ParseType parseType);

    /**
     * Generates the calculated value for an expression.
     *
     * @param expression the expression holding the formula for calculation.
     * @param parseType the type of expression to parse.
     * @return the calculated value.
     */
    Object getExpressionValue(String expression, ParseType parseType);

    /**
     * Generates the calculated numeric value for an expression.
     *
     * @param expression the expression holding the formula for calculation.
     * @param parseType the type of expression to parse.
     * @param valueMap the DimensionalItemObject values to use for calculation.
     * @param constantMap map of constants to use for calculation.
     * @param orgUnitCountMap the map of organisation unit group member counts.
     * @param days the number of days to use in the calculation.
     * @param missingValueStrategy the strategy to use when data values are
     *        missing when calculating the expression.
     * @return the calculated value as a double.
     */
    Double getExpressionValue(String expression, ParseType parseType,
        Map<DimensionalItemObject, Double> valueMap, Map<String, Constant> constantMap,
        Map<String, Integer> orgUnitCountMap, Integer days,
        MissingValueStrategy missingValueStrategy);

    /**
     * Generates the calculated value for an expression.
     *
     * @param expression the expression holding the formula for calculation.
     * @param parseType the type of expression to parse.
     * @param valueMap the DimensionalItemObject values to use for calculation.
     * @param constantMap map of constants to use for calculation.
     * @param orgUnitCountMap the map of organisation unit group member counts.
     * @param days the number of days to use in the calculation.
     * @param missingValueStrategy the strategy to use when data values are
     *        missing when calculating the expression.
     * @param samplePeriods periods for samples to aggregate.
     * @param periodValueMap values for aggregate functions by period.
     * @return the calculated value.
     */
    Object getExpressionValue(String expression, ParseType parseType,
        Map<DimensionalItemObject, Double> valueMap, Map<String, Constant> constantMap,
        Map<String, Integer> orgUnitCountMap, Integer days,
        MissingValueStrategy missingValueStrategy, List<Period> samplePeriods,
        MapMap<Period, DimensionalItemObject, Double> periodValueMap);
}
