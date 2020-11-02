package com.mass3d.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryOptionGroupSet;
import com.mass3d.dataelement.DataElementOperand;
import com.mass3d.dataentryform.DataEntryForm;
import com.mass3d.organisationunit.OrganisationUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.InterpretableObject;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.VersionedObject;
import com.mass3d.common.adapter.JacksonPeriodTypeDeserializer;
import com.mass3d.common.adapter.JacksonPeriodTypeSerializer;
import com.mass3d.dataelement.DataElement;
import com.mass3d.todotask.TodoTask;
import com.mass3d.indicator.Indicator;
import com.mass3d.interpretation.Interpretation;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.security.Authorities;
import com.mass3d.user.User;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTime;

//@Entity
//@Table(name = "dataset")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "datasetid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="datasetusergroupaccesses",
//        joinColumns=@JoinColumn(name="datasetid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="datasetuseraccesses",
//        joinColumns=@JoinColumn(name="datasetid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "dataSet", namespace = DxfNamespaces.DXF_2_0)
public class DataSet
    extends BaseDimensionalItemObject
    implements VersionedObject, MetadataObject, InterpretableObject {

  public static final int NO_EXPIRY = 0;

  @OneToMany(
      mappedBy = "dataSet",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<DataSetElement> dataSetElements = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "datasetsource",
      joinColumns = @JoinColumn(name = "datasetid", referencedColumnName = "datasetid"),
      inverseJoinColumns = @JoinColumn(name = "sourceid", referencedColumnName = "todotaskid")
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<TodoTask> todoTasks = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "datasetsource",
      joinColumns = @JoinColumn(name = "datasetid", referencedColumnName = "datasetid"),
      inverseJoinColumns = @JoinColumn(name = "sourceid", referencedColumnName = "organisationUnitid")
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<OrganisationUnit> sources = new HashSet<>();

  /**
   * The CategoryCombo used for data attributes.
   */
  private CategoryCombo categoryCombo;

  private String formName;
  /**
   * The PeriodType indicating the frequency that this DataSet should be used
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "periodtypeid")
  private PeriodType periodType;
  /**
   * The dataInputPeriods is a set of periods with opening and closing dates, which determines the
   * period of which data can belong (period) and at which dates (between opening and closing dates)
   * actually registering this data is allowed. The same period can exist at the same time with
   * different opening and closing dates to allow for multiple periods for registering data.
   */
  @OneToMany (cascade = CascadeType.ALL)
  @JoinTable(
      name = "datasetdatainputperiods",
      joinColumns = @JoinColumn(name = "datasetid", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "datainputperiodid", referencedColumnName = "id")
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<DataInputPeriod> dataInputPeriods = new HashSet<>();
  /**
   * Property indicating if the dataSet could be collected using mobile data entry.
   */
  private boolean mobile;

  /**
   * Indicating version number.
   */
  private int version;

  /**
   * How many days after period is over will this dataSet auto-lock
   */
  private int expiryDays;

  /**
   * Days after period end to qualify for timely data submission
   */
  private int timelyDays;

  /**
   * Number of periods to open for data capture that are after the category
   * option's end date.
   */
  private int openPeriodsAfterCoEndDate;

  /**
   * Indicators associated with this data set. Indicators are used for view and output purposes,
   * such as calculated elements in forms and reports.
   */
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "datasetindicators",
      joinColumns = @JoinColumn(name = "datasetid", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "indicatorid", referencedColumnName = "id")
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<Indicator> indicators = new HashSet<>();

  /**
   * User group which will receive notifications when data set is marked complete, can be null.
   */
  @OneToMany(mappedBy = "dataSet")
  private Set<Interpretation> interpretations = new HashSet<>();

  /**
   * The DataElementOperands for which data must be entered in order for the
   * DataSet to be considered as complete.
   */
  private Set<DataElementOperand> compulsoryDataElementOperands = new HashSet<>();

  /**
   * The Sections associated with the DataSet.
   */
  private Set<Section> sections = new HashSet<>();

  /**
   * Indicating custom data entry form, can be null.
   */
  private DataEntryForm dataEntryForm;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DataSet()
  {
  }

  public DataSet( String name )
  {
    this.name = name;
  }

  public DataSet( String name, PeriodType periodType )
  {
    this( name );
    this.periodType = periodType;
  }

  public DataSet( String name, String shortName, PeriodType periodType )
  {
    this( name, periodType );
    this.shortName = shortName;
  }

  public DataSet( String name, String shortName, String code, PeriodType periodType )
  {
    this( name, shortName, periodType );
    this.code = code;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public void addOrganisationUnit( OrganisationUnit organisationUnit )
  {
    sources.add( organisationUnit );
    organisationUnit.getDataSets().add( this );
  }

  public boolean removeOrganisationUnit( OrganisationUnit organisationUnit )
  {
    sources.remove( organisationUnit );
    return organisationUnit.getDataSets().remove( this );
  }

  public void removeAllOrganisationUnits()
  {
    for ( OrganisationUnit unit : sources )
    {
      unit.getDataSets().remove( this );
    }

    sources.clear();
  }

  public void updateOrganisationUnits( Set<OrganisationUnit> updates )
  {
    Set<OrganisationUnit> toRemove = Sets.difference( sources, updates );
    Set<OrganisationUnit> toAdd = Sets.difference( updates, sources );

    toRemove.forEach( u -> u.getDataSets().remove( this ) );
    toAdd.forEach( u -> u.getDataSets().add( this ) );

    sources.clear();
    sources.addAll( updates );
  }

  public boolean hasOrganisationUnit( OrganisationUnit unit )
  {
    return sources.contains( unit );
  }

  public boolean addDataSetElement( DataSetElement element )
  {
    element.getDataElement().getDataSetElements().add( element );
    return dataSetElements.add( element );
  }

  /**
   * Adds a data set element using this data set, the given data element and
   * no category combo.
   *
   * @param dataElement the data element.
   */
  public boolean addDataSetElement( DataElement dataElement )
  {
    DataSetElement element = new DataSetElement( this, dataElement, null );
    dataElement.getDataSetElements().add( element );
    return dataSetElements.add( element );
  }

  /**
   * Adds a data set element using this data set, the given data element and
   * the given category combo.
   *
   * @param dataElement   the data element.
   * @param categoryCombo the category combination.
   */
  public boolean addDataSetElement( DataElement dataElement, CategoryCombo categoryCombo )
  {
    DataSetElement element = new DataSetElement( this, dataElement, categoryCombo );
    dataElement.getDataSetElements().add( element );
    return dataSetElements.add( element );
  }

  public boolean removeDataSetElement( DataSetElement element )
  {
    dataSetElements.remove( element );
    return element.getDataElement().getDataSetElements().remove( element );
  }

  public void removeDataSetElement( DataElement dataElement )
  {
    Iterator<DataSetElement> elements = dataSetElements.iterator();

    while ( elements.hasNext() )
    {
      DataSetElement element = elements.next();

      DataSetElement other = new DataSetElement( this, dataElement );

      if ( element.objectEquals( other ) )
      {
        elements.remove();
        element.getDataElement().getDataSetElements().remove( element );
      }
    }
  }

  public void removeAllDataSetElements()
  {
    for ( DataSetElement element : dataSetElements )
    {
      element.getDataElement().getDataSetElements().remove( element );
    }

    dataSetElements.clear();
  }

  public Set<DataElement> getDataElements() {
    return ImmutableSet.copyOf(dataSetElements.stream().map(e -> e.getDataElement()).collect(Collectors
        .toSet()));
  }

  public boolean removeTodoTask(TodoTask todoTask) {
    todoTasks.remove(todoTask);
    return todoTask.getDataSets().remove(this);
  }

  public void addIndicator(Indicator indicator) {
    indicators.add(indicator);
    indicator.getDataSets().add(this);
  }

  public boolean removeIndicator(Indicator indicator) {
    indicators.remove(indicator);
    return indicator.getDataSets().remove(this);
  }

  public void addCompulsoryDataElementOperand( DataElementOperand dataElementOperand )
  {
    compulsoryDataElementOperands.add( dataElementOperand );
  }

  public void removeCompulsoryDataElementOperand( DataElementOperand dataElementOperand )
  {
    compulsoryDataElementOperands.remove( dataElementOperand );
  }

  public boolean hasDataEntryForm()
  {
    return dataEntryForm != null && dataEntryForm.hasForm();
  }

  public boolean hasSections()
  {
    return sections != null && sections.size() > 0;
  }

  @JsonProperty
  @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
  public FormType getFormType()
  {
    if ( hasDataEntryForm() )
    {
      return FormType.CUSTOM;
    }

    if ( hasSections() )
    {
      return FormType.SECTION;
    }

    return FormType.DEFAULT;
  }

  public Set<DataElement> getDataElementsInSections()
  {
    Set<DataElement> dataElements = new HashSet<>();

    for ( Section section : sections )
    {
      dataElements.addAll( section.getDataElements() );
    }

    return dataElements;
  }

  public Set<CategoryOptionCombo> getDataElementOptionCombos()
  {
    Set<CategoryOptionCombo> optionCombos = new HashSet<>();

    for ( DataSetElement element : dataSetElements )
    {
      optionCombos.addAll( element.getResolvedCategoryCombo().getOptionCombos() );
    }

    return optionCombos;
  }

  /**
   * Returns a set of category option group sets which are linked to this data
   * set through its category combination.
   */
  public Set<CategoryOptionGroupSet> getCategoryOptionGroupSets()
  {
    Set<CategoryOptionGroupSet> groupSets = new HashSet<>();

    if ( categoryCombo != null )
    {
      for ( Category category : categoryCombo.getCategories() )
      {
        for ( CategoryOption categoryOption : category.getCategoryOptions() )
        {
          groupSets.addAll( categoryOption.getGroupSets() );
        }
      }
    }

    return groupSets;
  }

  /**
   * Indicates whether this data set has a category combination which is different
   * from the default category combination.
   */
  public boolean hasCategoryCombo()
  {
    return categoryCombo != null && !CategoryCombo.DEFAULT_CATEGORY_COMBO_NAME.equals( categoryCombo.getName() );
  }

  /**
   * Indicates whether the data set is locked for data entry based on the expiry days.
   *
   * @param period the period to compare with.
   * @param now the date indicating now, uses current date if null.
   */
  public boolean isLocked(User user, Period period, Date now) {
    if (user != null && user.isAuthorized(Authorities.F_EDIT_EXPIRED.getAuthority())) {
      return false;
    }

    DateTime date = now != null ? new DateTime(now) : new DateTime();

    return expiryDays != DataSet.NO_EXPIRY &&
        new DateTime(period.getEndDate()).plusDays(expiryDays).isBefore(date);
  }

  /**
   * Checks if the given period and date combination conforms to any of the dataInputPeriods.
   * Returns true if no dataInputPeriods exists, or the combination conforms to at least one
   * dataInputPeriod.
   *
   * @return true if period and date conforms to a dataInputPeriod, or no dataInputPeriods exists.
   */
  public boolean isDataInputPeriodAndDateAllowed(Period period, Date date) {
    return dataInputPeriods.isEmpty() || dataInputPeriods.stream()
        .map(dataInputPeriod -> dataInputPeriod.isPeriodAndDateValid(period, date))
        .reduce((a, b) -> a || b)
        .orElse(true);
  }

  @Override
  public DimensionItemType getDimensionItemType() {
    return DimensionItemType.REPORTING_RATE;
  }
  // -------------------------------------------------------------------------
  // getters & setters
  // -------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(using = JacksonPeriodTypeSerializer.class)
  @JsonDeserialize(using = JacksonPeriodTypeDeserializer.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  @Property(PropertyType.TEXT)
  public PeriodType getPeriodType() {
    return periodType;
  }

  public void setPeriodType(PeriodType periodType) {
    this.periodType = periodType;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "dataInputPeriods", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "dataInputPeriods", namespace = DxfNamespaces.DXF_2_0)
  public Set<DataInputPeriod> getDataInputPeriods() {
    return dataInputPeriods;
  }

  public void setDataInputPeriods(Set<DataInputPeriod> dataInputPeriods) {
    this.dataInputPeriods = dataInputPeriods;
  }

  @JsonProperty
  @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
  public DataEntryForm getDataEntryForm()
  {
    return dataEntryForm;
  }

  public void setDataEntryForm( DataEntryForm dataEntryForm )
  {
    this.dataEntryForm = dataEntryForm;
  }

  @JsonProperty( value = "organisationUnits" )
  @JsonSerialize( contentAs = BaseIdentifiableObject.class )
  @JacksonXmlElementWrapper( localName = "organisationUnits", namespace = DxfNamespaces.DXF_2_0 )
  @JacksonXmlProperty( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0 )
  public Set<OrganisationUnit> getSources()
  {
    return sources;
  }

  public void setSources( Set<OrganisationUnit> sources )
  {
    this.sources = sources;
  }

  @JsonProperty
  @JacksonXmlElementWrapper( localName = "compulsoryDataElementOperands", namespace = DxfNamespaces.DXF_2_0 )
  @JacksonXmlProperty( localName = "compulsoryDataElementOperand", namespace = DxfNamespaces.DXF_2_0 )
  public Set<DataElementOperand> getCompulsoryDataElementOperands()
  {
    return compulsoryDataElementOperands;
  }

  public void setCompulsoryDataElementOperands( Set<DataElementOperand> compulsoryDataElementOperands )
  {
    this.compulsoryDataElementOperands = compulsoryDataElementOperands;
  }

  @JsonProperty
  @JsonSerialize( contentAs = BaseIdentifiableObject.class )
  @JacksonXmlElementWrapper( localName = "sections", namespace = DxfNamespaces.DXF_2_0 )
  @JacksonXmlProperty( localName = "section", namespace = DxfNamespaces.DXF_2_0 )
  public Set<Section> getSections()
  {
    return sections;
  }

  public void setSections( Set<Section> sections )
  {
    this.sections = sections;
  }

  @JsonProperty
  @JsonSerialize( as = BaseIdentifiableObject.class )
  @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
  public CategoryCombo getCategoryCombo()
  {
    return categoryCombo;
  }

  public void setCategoryCombo( CategoryCombo categoryCombo )
  {
    this.categoryCombo = categoryCombo;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public boolean isMobile() {
    return mobile;
  }

  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }


  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "dataSetElements", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "dataSetElement", namespace = DxfNamespaces.DXF_2_0)
  public Set<DataSetElement> getDataSetElements() {
    return dataSetElements;
  }

  public void setDataSetElements(Set<DataSetElement> dataSetElements) {
    this.dataSetElements = dataSetElements;
  }

  @JsonProperty(value = "todoTasks")
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "todoTasks", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "todoTask", namespace = DxfNamespaces.DXF_2_0)
  public Set<TodoTask> getTodoTasks() {
    return todoTasks;
  }

  public void setTodoTasks(Set<TodoTask> todoTasks) {
    this.todoTasks = todoTasks;
  }

  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "indicators", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "indicator", namespace = DxfNamespaces.DXF_2_0)
  public Set<Indicator> getIndicators() {
    return indicators;
  }

  public void setIndicators(Set<Indicator> indicators) {
    this.indicators = indicators;
  }

  @Override
  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "interpretations", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "interpretation", namespace = DxfNamespaces.DXF_2_0)
  public Set<Interpretation> getInterpretations() {
    return interpretations;
  }

  public void setInterpretations(Set<Interpretation> interpretations) {
    this.interpretations = interpretations;
  }

  @JsonProperty
  @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
  public int getOpenPeriodsAfterCoEndDate()
  {
    return openPeriodsAfterCoEndDate;
  }

  public void setOpenPeriodsAfterCoEndDate( int openPeriodsAfterCoEndDate )
  {
    this.openPeriodsAfterCoEndDate = openPeriodsAfterCoEndDate;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  @PropertyRange(min = Integer.MIN_VALUE)
  public int getExpiryDays() {
    return expiryDays;
  }

  public void setExpiryDays(int expiryDays) {
    this.expiryDays = expiryDays;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getTimelyDays() {
    return timelyDays;
  }

  public void setTimelyDays(int timelyDays) {
    this.timelyDays = timelyDays;
  }

  @Override
  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public int increaseVersion() {
    return ++version;
  }

  @Override
  @JsonProperty
  @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
  public String getFormName()
  {
    return formName;
  }

  @Override
  public void setFormName( String formName )
  {
    this.formName = formName;
  }


}
