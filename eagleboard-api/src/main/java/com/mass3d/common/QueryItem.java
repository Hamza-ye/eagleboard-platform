package com.mass3d.common;

import com.mass3d.dataelement.DataElement;
import com.mass3d.option.OptionSet;
import com.mass3d.program.Program;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.analytics.AggregationType;
import com.mass3d.analytics.QueryKey;

/**
 * Class which encapsulates a query parameter and value. Operator and filter
 * are inherited from QueryFilter.
 *
 */
public class QueryItem
{
  private DimensionalItemObject item; // TODO DimensionObject

//  private LegendSet legendSet;

  private List<QueryFilter> filters = new ArrayList<>();

  private ValueType valueType;

  private AggregationType aggregationType;

  private OptionSet optionSet;

  private Program program;

  private Boolean unique = false;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryItem( DimensionalItemObject item )
  {
    this.item = item;
  }

  public QueryItem( DimensionalItemObject item, ValueType valueType, AggregationType aggregationType, OptionSet optionSet )
  {
    this.item = item;
//    this.legendSet = legendSet;
    this.valueType = valueType;
    this.aggregationType = aggregationType;
    this.optionSet = optionSet;
  }

  public QueryItem( DimensionalItemObject item, ValueType valueType, AggregationType aggregationType, OptionSet optionSet, Boolean unique )
  {
    this.item = item;
//    this.legendSet = legendSet;
    this.valueType = valueType;
    this.aggregationType = aggregationType;
    this.optionSet = optionSet;
    this.unique = unique;
  }

  public QueryItem( DimensionalItemObject item, QueryOperator operator, String filter, ValueType valueType, AggregationType aggregationType, OptionSet optionSet )
  {
    this.item = item;
    this.valueType = valueType;
    this.aggregationType = aggregationType;
    this.optionSet = optionSet;

    if ( operator != null && filter != null )
    {
      this.filters.add( new QueryFilter( operator, filter ) );
    }
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public String getItemId()
  {
    return item.getUid();
  }

  public String getItemName()
  {
    String itemName = item.getUid();

//    if ( legendSet != null )
//    {
//      itemName += "_" + legendSet.getUid();
//    }

    return itemName;
  }

  public boolean addFilter( QueryFilter filter )
  {
    return filters.add( filter );
  }

  public String getKey()
  {
    QueryKey key = new QueryKey();

    key.add( getItemId() ).addIgnoreNull( getFiltersAsString() );

//    if ( legendSet != null )
//    {
//      key.add( legendSet.getUid() );
//    }

    return key.build();
  }

  /**
   * Returns a string representation of the query filters. Returns null if item
   * has no query items.
   */
  public String getFiltersAsString()
  {
    if ( filters.isEmpty() )
    {
      return null;
    }

    List<String> filterStrings = filters.stream().map( QueryFilter::getFilterAsString ).collect( Collectors.toList() );
    return StringUtils.join( filterStrings, ", " );
  }

  public String getTypeAsString()
  {
    return valueType.getJavaClass().getName();
  }

  public boolean isNumeric()
  {
    return valueType.isNumeric();
  }

  public boolean isText()
  {
    return valueType.isText();
  }

//  public boolean hasLegendSet()
//  {
//    return legendSet != null;
//  }

  public boolean hasOptionSet()
  {
    return optionSet != null;
  }

  public boolean hasFilter()
  {
    return filters != null && !filters.isEmpty();
  }

  public boolean hasProgram()
  {
    return program != null;
  }

  public boolean isProgramIndicator()
  {
    return DimensionItemType.PROGRAM_INDICATOR.equals( item.getDimensionItemType() );
  }

//  /**
//   * Returns filter items for all filters associated with this
//   * query item. If no filter items are specified, return all
//   * items part of the legend set. If not legend set is specified,
//   * returns null.
//   */
//  public List<String> getLegendSetFilterItemsOrAll()
//  {
//    if ( !hasLegendSet() )
//    {
//      return null;
//    }
//
//    return hasFilter() ? getQueryFilterItems() :
//        IdentifiableObjectUtils.getUids( legendSet.getSortedLegends() );
//  }

  /**
   * Returns filter items for all filters associated with this
   * query item. If no filter items are specified, return all
   * items part of the option set. If not option set is specified,
   * returns null.
   */
  public List<String> getOptionSetFilterItemsOrAll()
  {
    if ( !hasOptionSet() )
    {
      return null;
    }

    return hasFilter() ? getOptionSetQueryFilterItems() :
        IdentifiableObjectUtils.getUids( optionSet.getOptions() );
  }

  /**
   * Returns option filter items. Options are specified by code
   * but returned as identifiers, so the codes are mapped to
   * options and then to identifiers.
   *
   * //TODO clean up and standardize on identifier.
   */
  private List<String> getOptionSetQueryFilterItems()
  {
    return getQueryFilterItems().stream()
        .map( code -> optionSet.getOptionByCode( code ) )
        .filter( option -> option != null )
        .map( option -> option.getUid() )
        .collect( Collectors.toList() );
  }

  /**
   * Returns filter items for all filters associated with this
   * query item.
   */
  public List<String> getQueryFilterItems()
  {
    List<String> filterItems = new ArrayList<>();
    filters.forEach( f -> filterItems.addAll( QueryFilter.getFilterItems( f.getFilter() ) ) );
    return filterItems;
  }

  /**
   * Returns SQL filter for the given query filter and SQL encoded
   * filter. If the item value type is text-based, the filter is
   * converted to lower-case.
   *
   * @param filter the query filter.
   * @param encodedFilter the SQL encoded filter.
   */
  public String getSqlFilter( QueryFilter filter, String encodedFilter )
  {
    String sqlFilter = filter.getSqlFilter( encodedFilter );

    return isText() ? sqlFilter.toLowerCase() : sqlFilter;
  }

  // -------------------------------------------------------------------------
  // Eagle Board Static utilities
  // -------------------------------------------------------------------------

  public static List<QueryItem> getDataFieldQueryItems( Collection<DataElement> dataElements)
  {
    List<QueryItem> queryItems = new ArrayList<>();

    for ( DataElement dataElement : dataElements)
    {
      queryItems.add( new QueryItem(dataElement, dataElement.getValueType(), dataElement.getAggregationType(), dataElement
          .hasOptionSet() ? dataElement.getOptionSet() : null ) );
    }

    return queryItems;
  }

  // -------------------------------------------------------------------------
  // Static utilities
  // -------------------------------------------------------------------------

  public static List<QueryItem> getQueryItems( Collection<TrackedEntityAttribute> attributes )
  {
    List<QueryItem> queryItems = new ArrayList<>();

    for ( TrackedEntityAttribute attribute : attributes )
    {
      queryItems.add( new QueryItem( attribute, attribute.getValueType(), attribute.getAggregationType(), attribute.hasOptionSet() ? attribute.getOptionSet() : null ) );
    }

    return queryItems;
  }

  public static List<QueryItem> getDataElementQueryItems( Collection<DataElement> dataElements )
  {
    List<QueryItem> queryItems = new ArrayList<>();

    for ( DataElement dataElement : dataElements )
    {
      queryItems.add( new QueryItem( dataElement, dataElement.getValueType(), dataElement.getAggregationType(), dataElement.hasOptionSet() ? dataElement.getOptionSet() : null ) );
    }

    return queryItems;
  }

  // -------------------------------------------------------------------------
  // hashCode, equals and toString
  // -------------------------------------------------------------------------

  @Override
  public int hashCode()
  {
    return item.hashCode();
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object == null )
    {
      return false;
    }

    if ( getClass() != object.getClass() )
    {
      return false;
    }

    final QueryItem other = (QueryItem) object;

    return item.equals( other.getItem() );
  }

  @Override
  public String toString()
  {
    return "[Item: " + item + ", filters: " + filters + ", value type: " + valueType + ", optionSet: " + optionSet + "]";
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  public DimensionalItemObject getItem()
  {
    return item;
  }

  public void setItem( DimensionalItemObject item )
  {
    this.item = item;
  }

//  public LegendSet getLegendSet()
//  {
//    return legendSet;
//  }
//
//  public void setLegendSet( LegendSet legendSet )
//  {
//    this.legendSet = legendSet;
//  }

  public List<QueryFilter> getFilters()
  {
    return filters;
  }

  public void setFilters( List<QueryFilter> filters )
  {
    this.filters = filters;
  }

  public ValueType getValueType()
  {
    return valueType;
  }

  public void setValueType( ValueType valueType )
  {
    this.valueType = valueType;
  }

  public AggregationType getAggregationType()
  {
    return aggregationType;
  }

  public void setAggregationType( AggregationType aggregationType )
  {
    this.aggregationType = aggregationType;
  }

  public OptionSet getOptionSet()
  {
    return optionSet;
  }

  public void setOptionSet( OptionSet optionSet )
  {
    this.optionSet = optionSet;
  }

  public Program getProgram()
  {
    return program;
  }

  public void setProgram( Program program )
  {
    this.program = program;
  }

  public Boolean isUnique()
  {
    return unique;
  }

  public void setUnique( Boolean unique )
  {
    this.unique = unique;
  }
}
