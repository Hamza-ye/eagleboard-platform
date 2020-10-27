package com.mass3d.datavalue;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mass3d.common.IdSchemes;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataset.DataSet;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.todotask.TodoTask;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DataExportParams {

  private Set<DataElement> dataElements = new HashSet<>();

//    private Set<DataElementOperand> dataElementOperands = new HashSet<>();

  private Set<DataSet> dataSets = new HashSet<>();

  private Set<DataElementGroup> dataElementGroups = new HashSet<>();

  private Set<Period> periods = new HashSet<>();

  private Set<PeriodType> periodTypes = new HashSet<>();

  private Date startDate;

  private Date endDate;

  private Date includedDate;

  private Set<TodoTask> todoTasks = new HashSet<>();

  private boolean includeChildren;

  private boolean returnParentOrgUnit;

//    private Set<OrganisationUnitGroup> organisationUnitGroups = new HashSet<>();
//
//    private Set<CategoryOptionCombo> attributeOptionCombos = new HashSet<>();
//
//    private Set<CategoryOption> coDimensionConstraints;
//
//    private Set<CategoryOptionGroup> cogDimensionConstraints;

  private boolean includeDeleted;

  private Date lastUpdated;

  private String lastUpdatedDuration;

  private Integer limit;

  private IdSchemes outputIdSchemes;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DataExportParams() {
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public Set<DataElement> getAllDataElements() {
    final Set<DataElement> elements = Sets.newHashSet();

    elements.addAll(dataElements);
    dataSets.forEach(ds -> elements.addAll(ds.getDataElements()));
        dataElementGroups.forEach( dg -> elements.addAll( dg.getMembers() ) );

    return ImmutableSet.copyOf(elements);
  }

  public Set<TodoTask> getAllTodoTasks() {
    final Set<TodoTask> orgUnits = Sets.newHashSet();
    orgUnits.addAll(todoTasks);

//        for ( OrganisationUnitGroup group : organisationUnitGroups )
//        {
//            orgUnits.addAll( group.getMembers() );
//        }

    return ImmutableSet.copyOf(orgUnits);
  }

  public boolean hasDataElements() {
    return dataElements != null && !dataElements.isEmpty();
  }

//    public boolean hasDataElementOperands()
//    {
//        return dataElementOperands != null && !dataElementOperands.isEmpty();
//    }

  public boolean hasDataSets() {
    return dataSets != null && !dataSets.isEmpty();
  }

  public boolean hasDataElementGroups() {
    return dataElementGroups != null && !dataElementGroups.isEmpty();
  }

  public DataSet getFirstDataSet() {
    return dataSets != null && !dataSets.isEmpty() ? dataSets.iterator().next() : null;
  }

  public Period getFirstPeriod() {
    return periods != null && !periods.isEmpty() ? periods.iterator().next() : null;
  }

  public boolean hasPeriods() {
    return periods != null && !periods.isEmpty();
  }

  public boolean hasPeriodTypes() {
    return periodTypes != null && !periodTypes.isEmpty();
  }

  public boolean hasStartEndDate() {
    return startDate != null && endDate != null;
  }

  public boolean hasIncludedDate() {
    return includedDate != null;
  }

  public boolean hasTodoTasks() {
    return todoTasks != null && !todoTasks.isEmpty();
  }

  public boolean isIncludeChildrenForOrganisationUnits() {
    return includeChildren && hasTodoTasks();
  }

  public boolean isReturnParentForOrganisationUnits() {
    return returnParentOrgUnit && hasTodoTasks();
  }

  public TodoTask getFirstTodoTask() {
    return todoTasks != null && !todoTasks.isEmpty() ? todoTasks.iterator().next() : null;
  }

//    public boolean hasOrganisationUnitGroups()
//    {
//        return organisationUnitGroups != null && !organisationUnitGroups.isEmpty();
//    }
//
//    public boolean hasAttributeOptionCombos()
//    {
//        return attributeOptionCombos != null && !attributeOptionCombos.isEmpty();
//    }
//
//    public boolean hasCoDimensionConstraints()
//    {
//        return coDimensionConstraints != null && !coDimensionConstraints.isEmpty();
//    }
//
//    public boolean hasCogDimensionConstraints()
//    {
//        return cogDimensionConstraints != null && !cogDimensionConstraints.isEmpty();
//    }

  public boolean hasLastUpdated() {
    return lastUpdated != null;
  }

  public boolean hasLastUpdatedDuration() {
    return lastUpdatedDuration != null;
  }

  public boolean hasLimit() {
    return limit != null;
  }

  /**
   * Indicates whether this parameters represents a single data value set, implying that it contains
   * exactly one of data sets, periods and organisation units.
   */
  public boolean isSingleDataValueSet() {
    return dataSets.size() == 1 && periods.size() == 1 && todoTasks.size() == 1;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).
        add("data fields", dataElements).
//            add( "data element operands", dataElementOperands ).
    add("field sets", dataSets).
//            add( "data element groups", dataElementGroups ).
    add("periods", periods).
            add("period types", periodTypes).
            add("start date", startDate).
            add("end date", endDate).
            add("included date", includedDate).
            add("todo tasks", todoTasks).
            add("children", includeChildren).
            add("return parent org unit", returnParentOrgUnit).
//            add( "org unit groups", organisationUnitGroups ).
//            add( "attribute option combos", attributeOptionCombos ).
//            add( "category option dimension constraints", coDimensionConstraints ).
//            add( "category option group dimension constraints", cogDimensionConstraints ).
    add("deleted", includeDeleted).
            add("last updated", lastUpdated).
            add("last updated duration", lastUpdatedDuration).
            add("limit", limit).
            add("output id schemes", outputIdSchemes).toString();
  }

  // -------------------------------------------------------------------------
  // Get and set methods
  // -------------------------------------------------------------------------

  public Set<DataElement> getDataElements() {
    return dataElements;
  }

  public DataExportParams setDataElements(Set<DataElement> dataElements) {
    this.dataElements = dataElements;
    return this;
  }

//    public Set<DataElementOperand> getDataElementOperands()
//    {
//        return dataElementOperands;
//    }
//
//    public DataExportParams setDataElementOperands( Set<DataElementOperand> dataElementOperands )
//    {
//        this.dataElementOperands = dataElementOperands;
//        return this;
//    }

  public Set<DataSet> getDataSets() {
    return dataSets;
  }

  public DataExportParams setDataSets(Set<DataSet> dataSets) {
    this.dataSets = dataSets;
    return this;
  }

    public Set<DataElementGroup> getDataElementGroups()
    {
        return dataElementGroups;
    }

    public DataExportParams setDataElementGroups( Set<DataElementGroup> dataElementGroups )
    {
        this.dataElementGroups = dataElementGroups;
        return this;
    }

  public Set<PeriodType> getPeriodTypes() {
    return periodTypes;
  }

  public DataExportParams setPeriodTypes(Set<PeriodType> periodTypes) {
    this.periodTypes = periodTypes;
    return this;
  }

  public Set<Period> getPeriods() {
    return periods;
  }

  public DataExportParams setPeriods(Set<Period> periods) {
    this.periods = periods;
    return this;
  }

  public Date getStartDate() {
    return startDate;
  }

  public DataExportParams setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public Date getIncludedDate() {
    return includedDate;
  }

  public DataExportParams setIncludedDate(Date includedDate) {
    this.includedDate = includedDate;
    return this;
  }

  public Date getEndDate() {
    return endDate;
  }

  public DataExportParams setEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public Set<TodoTask> getTodoTasks() {
    return todoTasks;
  }

  public DataExportParams setTodoTasks(Set<TodoTask> todoTasks) {
    this.todoTasks = todoTasks;
    return this;
  }

  public boolean isIncludeChildren() {
    return includeChildren;
  }

  public DataExportParams setIncludeChildren(boolean includeChildren) {
    this.includeChildren = includeChildren;
    return this;
  }

  public boolean isReturnParentOrgUnit() {
    return returnParentOrgUnit;
  }

  public DataExportParams setReturnParentOrgUnit(boolean returnParentOrgUnit) {
    this.returnParentOrgUnit = returnParentOrgUnit;
    return this;
  }

//    public Set<OrganisationUnitGroup> getOrganisationUnitGroups()
//    {
//        return organisationUnitGroups;
//    }
//
//    public DataExportParams setOrganisationUnitGroups( Set<OrganisationUnitGroup> organisationUnitGroups )
//    {
//        this.organisationUnitGroups = organisationUnitGroups;
//        return this;
//    }

//    public Set<CategoryOptionCombo> getAttributeOptionCombos()
//    {
//        return attributeOptionCombos;
//    }
//
//    public DataExportParams setAttributeOptionCombos( Set<CategoryOptionCombo> attributeOptionCombos )
//    {
//        this.attributeOptionCombos = attributeOptionCombos;
//        return this;
//    }
//
//    public Set<CategoryOption> getCoDimensionConstraints()
//    {
//        return coDimensionConstraints;
//    }
//
//    public DataExportParams setCoDimensionConstraints( Set<CategoryOption> coDimensionConstraints )
//    {
//        this.coDimensionConstraints = coDimensionConstraints;
//        return this;
//    }
//
//    public Set<CategoryOptionGroup> getCogDimensionConstraints()
//    {
//        return cogDimensionConstraints;
//    }
//
//    public DataExportParams setCogDimensionConstraints( Set<CategoryOptionGroup> cogDimensionConstraints )
//    {
//        this.cogDimensionConstraints = cogDimensionConstraints;
//        return this;
//    }

  public boolean isIncludeDeleted() {
    return includeDeleted;
  }

  public DataExportParams setIncludeDeleted(boolean includeDeleted) {
    this.includeDeleted = includeDeleted;
    return this;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public DataExportParams setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public String getLastUpdatedDuration() {
    return lastUpdatedDuration;
  }

  public DataExportParams setLastUpdatedDuration(String lastUpdatedDuration) {
    this.lastUpdatedDuration = lastUpdatedDuration;
    return this;
  }

  public Integer getLimit() {
    return limit;
  }

  public DataExportParams setLimit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public IdSchemes getOutputIdSchemes() {
    return outputIdSchemes;
  }

  public DataExportParams setOutputIdSchemes(IdSchemes outputIdSchemes) {
    this.outputIdSchemes = outputIdSchemes;
    return this;
  }
}
