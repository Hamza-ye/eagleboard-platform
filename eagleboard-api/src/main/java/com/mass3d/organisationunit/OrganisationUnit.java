package com.mass3d.organisationunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geojson.geom.GeometryJSON;
import com.mass3d.category.CategoryOption;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionItemType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.IdentifiableObjectUtils;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.SortProperty;
import com.mass3d.common.adapter.JacksonOrganisationUnitChildrenSerializer;
import com.mass3d.common.coordinate.CoordinateObject;
import com.mass3d.common.coordinate.CoordinateUtils;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataset.DataSet;
import com.mass3d.organisationunit.comparator.OrganisationUnitDisplayNameComparator;
import com.mass3d.organisationunit.comparator.OrganisationUnitDisplayShortNameComparator;
import com.mass3d.program.Program;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.user.User;

@JacksonXmlRootElement( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0 )
public class OrganisationUnit
    extends
    BaseDimensionalItemObject
    implements
    MetadataObject,
    CoordinateObject
{
    private static final String PATH_SEP = "/";

    public static final String KEY_USER_ORGUNIT = "USER_ORGUNIT";

    public static final String KEY_USER_ORGUNIT_CHILDREN = "USER_ORGUNIT_CHILDREN";

    public static final String KEY_USER_ORGUNIT_GRANDCHILDREN = "USER_ORGUNIT_GRANDCHILDREN";

    public static final String KEY_LEVEL = "LEVEL-";

    public static final String KEY_ORGUNIT_GROUP = "OU_GROUP-";

    private static final String NAME_SEPARATOR = " / ";

    private OrganisationUnit parent;

    private String path;

    private Integer hierarchyLevel;

    private Date openingDate;

    private Date closedDate;

    private String comment;

    private String url;

    private String contactPerson;

    private String address;

    private String email;

    private String phoneNumber;

    private Set<OrganisationUnitGroup> groups = new HashSet<>();

    private Set<DataSet> dataSets = new HashSet<>();

    private Set<Program> programs = new HashSet<>();

    private Set<User> users = new HashSet<>();

    private Set<CategoryOption> categoryOptions = new HashSet<>();

    private Geometry geometry;

    // -------------------------------------------------------------------------
    // Transient fields
    // -------------------------------------------------------------------------

    private Set<OrganisationUnit> children = new HashSet<>();

    private transient boolean currentParent;

    private transient String type;

    private transient List<String> groupNames = new ArrayList<>();

    private transient Double value;

    private transient Integer memberCount;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public OrganisationUnit()
    {
        setAutoFields(); // Must be set to get UID and have getPath work properly
    }

    public OrganisationUnit( String name )
    {
        this();
        this.name = name;
    }

    /**
     * @param name OrgUnit name
     * @param shortName OrgUnit short name
     * @param code OrgUnit code
     * @param openingDate OrgUnit opening date
     * @param closedDate OrgUnit closing date
     * @param comment a comment
     */
    public OrganisationUnit( String name, String shortName, String code, Date openingDate, Date closedDate,
        String comment )
    {
        this( name );
        this.shortName = shortName;
        this.code = code;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.comment = comment;
    }

    /**
     * @param name OrgUnit name
     * @param parent parent {@link OrganisationUnit}
     * @param shortName OrgUnit short name
     * @param code OrgUnit code
     * @param openingDate OrgUnit opening date
     * @param closedDate OrgUnit closing date
     * @param comment a comment
     */
    public OrganisationUnit( String name, OrganisationUnit parent, String shortName, String code, Date openingDate,
        Date closedDate, String comment )
    {
        this( name );
        this.parent = parent;
        this.shortName = shortName;
        this.code = code;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.comment = comment;
    }

    @Override
    public void setAutoFields()
    {
        super.setAutoFields();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        groups.add( organisationUnitGroup );
        organisationUnitGroup.getMembers().add( this );
    }

    public void removeOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        groups.remove( organisationUnitGroup );
        organisationUnitGroup.getMembers().remove( this );
    }

    public void removeAllOrganisationUnitGroups()
    {
        for ( OrganisationUnitGroup organisationUnitGroup : groups )
        {
            organisationUnitGroup.getMembers().remove( this );
        }

        groups.clear();
    }

    public void addDataSet( DataSet dataSet )
    {
        dataSets.add( dataSet );
        dataSet.getSources().add( this );
    }

    public void removeDataSet( DataSet dataSet )
    {
        dataSets.remove( dataSet );
        dataSet.getSources().remove( this );
    }

    public void removeAllDataSets()
    {
        for ( DataSet dataSet : dataSets )
        {
            dataSet.getSources().remove( this );
        }

        dataSets.clear();
    }

    public void updateDataSets( Set<DataSet> updates )
    {
        Set<DataSet> toRemove = Sets.difference( dataSets, updates );
        Set<DataSet> toAdd = Sets.difference( updates, dataSets );

        toRemove.forEach( d -> d.getSources().remove( this ) );
        toAdd.forEach( d -> d.getSources().add( this ) );

        dataSets.clear();
        dataSets.addAll( updates );
    }

    public void addUser( User user )
    {
        users.add( user );
        user.getOrganisationUnits().add( this );
    }

    public void removeUser( User user )
    {
        users.remove( user );
        user.getOrganisationUnits().remove( this );
    }

    public void addCategoryOption( CategoryOption categoryOption )
    {
        categoryOptions.add( categoryOption );
        categoryOption.getOrganisationUnits().add( this );
    }

    public void removeCategoryOption( CategoryOption categoryOption )
    {
        categoryOptions.remove( categoryOption );
        categoryOption.getOrganisationUnits().remove( this );
    }

    public void removeAllUsers()
    {
        for ( User user : users )
        {
            user.getOrganisationUnits().remove( this );
        }

        users.clear();
    }

    public List<OrganisationUnit> getSortedChildren( SortProperty sortBy )
    {
        List<OrganisationUnit> sortedChildren = new ArrayList<>( children );

        Comparator<OrganisationUnit> comparator = SortProperty.SHORT_NAME == sortBy
            ? OrganisationUnitDisplayShortNameComparator.INSTANCE
            : OrganisationUnitDisplayNameComparator.INSTANCE;

        Collections.sort( sortedChildren, comparator );
        return sortedChildren;

    }

    public List<OrganisationUnit> getSortedChildren()
    {
        return getSortedChildren( SortProperty.NAME );
    }

    public static List<OrganisationUnit> getSortedChildren( Collection<OrganisationUnit> units )
    {
        return getSortedChildren( units, SortProperty.NAME );
    }

    public static List<OrganisationUnit> getSortedChildren( Collection<OrganisationUnit> units, SortProperty sortBy )
    {
        List<OrganisationUnit> children = new ArrayList<>();

        for ( OrganisationUnit unit : units )
        {
            children.addAll( unit.getSortedChildren( sortBy ) );
        }

        return children;
    }

    public static List<OrganisationUnit> getSortedGrandChildren( Collection<OrganisationUnit> units )
    {
        return getSortedGrandChildren( units, SortProperty.NAME );
    }

    public static List<OrganisationUnit> getSortedGrandChildren( Collection<OrganisationUnit> units,
        SortProperty sortBy )
    {
        List<OrganisationUnit> children = new ArrayList<>();

        for ( OrganisationUnit unit : units )
        {
            children.addAll( unit.getSortedGrandChildren( sortBy ) );
        }

        return children;
    }

    public Set<OrganisationUnit> getGrandChildren()
    {
        Set<OrganisationUnit> grandChildren = new HashSet<>();

        for ( OrganisationUnit child : children )
        {
            grandChildren.addAll( child.getChildren() );
        }

        return grandChildren;
    }

    public List<OrganisationUnit> getSortedGrandChildren()
    {
        return getSortedGrandChildren( SortProperty.NAME );
    }

    public List<OrganisationUnit> getSortedGrandChildren( SortProperty sortBy )
    {
        List<OrganisationUnit> grandChildren = new ArrayList<>();

        for ( OrganisationUnit child : getSortedChildren( sortBy ) )
        {
            grandChildren.addAll( child.getSortedChildren( sortBy ) );
        }

        return grandChildren;
    }

    public boolean hasChild()
    {
        return !this.children.isEmpty();
    }

    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    public boolean isLeaf()
    {
        return children == null || children.isEmpty();
    }

    @Override
    public boolean hasDescendantsWithCoordinates()
    {
        return CoordinateUtils.hasDescendantsWithCoordinates( children );
    }

    public boolean isDescendant( OrganisationUnit ancestor )
    {
        if ( ancestor == null )
        {
            return false;
        }

        OrganisationUnit unit = this;

        while ( unit != null )
        {
            if ( ancestor.equals( unit ) )
            {
                return true;
            }

            unit = unit.getParent();
        }

        return false;
    }

    public boolean isDescendant( Set<OrganisationUnit> ancestors )
    {
        if ( ancestors == null || ancestors.isEmpty() )
        {
            return false;
        }
        Set<String> ancestorsUid = ancestors.stream().map( OrganisationUnit::getUid ).collect( Collectors
            .toSet() );

        OrganisationUnit unit = this;

        while ( unit != null )
        {
            if ( ancestorsUid.contains( unit.getUid() ) )
            {
                return true;
            }

            unit = unit.getParent();
        }

        return false;
    }

    public boolean hasCoordinatesUp()
    {
        if ( parent != null )
        {
            if ( parent.getParent() != null )
            {
                return parent.getParent().hasDescendantsWithCoordinates();
            }
        }

        return false;
    }

    public OrganisationUnitGroup getGroupInGroupSet( OrganisationUnitGroupSet groupSet )
    {
        if ( groupSet != null )
        {
            for ( OrganisationUnitGroup group : groups )
            {
                if ( groupSet.getOrganisationUnitGroups().contains( group ) )
                {
                    return group;
                }
            }
        }

        return null;
    }

    public Long getGroupIdInGroupSet( OrganisationUnitGroupSet groupSet )
    {
        final OrganisationUnitGroup group = getGroupInGroupSet( groupSet );

        return group != null ? group.getId() : null;
    }

    public String getGroupNameInGroupSet( OrganisationUnitGroupSet groupSet )
    {
        final OrganisationUnitGroup group = getGroupInGroupSet( groupSet );

        return group != null ? group.getName() : null;
    }

    public String getAncestorNames()
    {
        List<OrganisationUnit> units = getAncestors();

        StringBuilder builder = new StringBuilder();

        for ( OrganisationUnit unit : units )
        {
            builder.append( unit.getName() ).append( NAME_SEPARATOR );
        }

        return builder.toString();
    }

    /**
     * Returns the list of ancestor organisation units for this organisation unit.
     * Does not include itself. The list is ordered by root first.
     *
     * @throws IllegalStateException if circular parent relationships is detected.
     */
    @JsonProperty( "ancestors" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "ancestors", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "organisationUnit", namespace = DxfNamespaces.DXF_2_0 )
    public List<OrganisationUnit> getAncestors()
    {
        List<OrganisationUnit> units = new ArrayList<>();
        Set<OrganisationUnit> visitedUnits = new HashSet<>();

        OrganisationUnit unit = parent;

        while ( unit != null )
        {
            if ( !visitedUnits.add( unit ) )
            {
                throw new IllegalStateException(
                    "Organisation unit '" + this.toString() + "' has circular parent relationships: '" + unit + "'" );
            }

            units.add( unit );
            unit = unit.getParent();
        }

        Collections.reverse( units );
        return units;
    }

    /**
     * Returns the list of ancestor organisation units up to any of the given roots
     * for this organisation unit. Does not include itself. The list is ordered by
     * root first.
     *
     * @param roots the root organisation units, if null using real roots.
     */
    public List<OrganisationUnit> getAncestors( Collection<OrganisationUnit> roots )
    {
        List<OrganisationUnit> units = new ArrayList<>();
        OrganisationUnit unit = parent;

        while ( unit != null )
        {
            units.add( unit );

            if ( roots != null && roots.contains( unit ) )
            {
                break;
            }

            unit = unit.getParent();
        }

        Collections.reverse( units );
        return units;
    }

    /**
     * Returns the list of ancestor organisation unit names up to any of the given
     * roots for this organisation unit. The list is ordered by root first.
     *
     * @param roots the root organisation units, if null using real roots.
     */
    public List<String> getAncestorNames( Collection<OrganisationUnit> roots, boolean includeThis )
    {
        List<String> units = new ArrayList<>();

        if ( includeThis )
        {
            units.add( getDisplayName() );
        }

        OrganisationUnit unit = parent;

        while ( unit != null )
        {
            units.add( unit.getDisplayName() );

            if ( roots != null && roots.contains( unit ) )
            {
                break;
            }

            unit = unit.getParent();
        }

        Collections.reverse( units );
        return units;
    }

    /**
     * Returns the list of ancestor organisation unit UIDs up to any of the given
     * roots for this organisation unit. Does not include itself. The list is
     * ordered by root first.
     *
     * @param rootUids the root organisation units, if null using real roots.
     */
    public List<String> getAncestorUids( Set<String> rootUids )
    {
        if ( path == null || path.isEmpty() )
        {
            return Lists.newArrayList();
        }

        String[] ancestors = path.substring( 1 ).split( PATH_SEP ); // Skip first delimiter, root unit first
        int lastIndex = ancestors.length - 2; // Skip this unit
        List<String> uids = Lists.newArrayList();

        for ( int i = lastIndex; i >= 0; i-- )
        {
            String uid = ancestors[i];
            uids.add( 0, uid );

            if ( rootUids != null && rootUids.contains( uid ) )
            {
                break;
            }
        }

        return uids;
    }

    public void updateParent( OrganisationUnit newParent )
    {
        if ( this.parent != null && this.parent.getChildren() != null )
        {
            this.parent.getChildren().remove( this );
        }

        this.parent = newParent;

        newParent.getChildren().add( this );
    }

    public Set<OrganisationUnit> getChildrenThisIfEmpty()
    {
        Set<OrganisationUnit> set = new HashSet<>();

        if ( hasChild() )
        {
            set = children;
        }
        else
        {
            set.add( this );
        }

        return set;
    }

    @JsonProperty( value = "level", access = JsonProperty.Access.READ_ONLY )
    @JacksonXmlProperty( localName = "level", isAttribute = true )
    public int getLevel()
    {
        return StringUtils.countMatches( path, PATH_SEP );
    }

    protected void setLevel( int level )
    {
        // ignored, just used by persistence framework
    }

    /**
     * Returns a string representing the graph of ancestors. The string is delimited
     * by "/". The ancestors are ordered by root first and represented by UIDs.
     *
     * @param roots the root organisation units, if null using real roots.
     */
    public String getParentGraph( Collection<OrganisationUnit> roots )
    {
        Set<String> rootUids = roots != null ? Sets.newHashSet( IdentifiableObjectUtils.getUids( roots ) ) : null;
        List<String> ancestors = getAncestorUids( rootUids );
        return StringUtils.join( ancestors, PATH_SEP );
    }

    /**
     * Returns a string representing the graph of ancestors. The string is delimited
     * by "/". The ancestors are ordered by root first and represented by names.
     *
     * @param roots the root organisation units, if null using real roots.
     * @param includeThis whether to include this organisation unit in the graph.
     */
    public String getParentNameGraph( Collection<OrganisationUnit> roots, boolean includeThis )
    {
        StringBuilder builder = new StringBuilder();

        List<OrganisationUnit> ancestors = getAncestors( roots );

        for ( OrganisationUnit unit : ancestors )
        {
            builder.append( "/" ).append( unit.getName() );
        }

        if ( includeThis )
        {
            builder.append( "/" ).append( name );
        }

        return builder.toString();
    }

    /**
     * Returns a mapping between the uid and the uid parent graph of the given
     * organisation units.
     */
    public static Map<String, String> getParentGraphMap( List<OrganisationUnit> organisationUnits,
        Collection<OrganisationUnit> roots )
    {
        Map<String, String> map = new HashMap<>();

        if ( organisationUnits != null )
        {
            for ( OrganisationUnit unit : organisationUnits )
            {
                map.put( unit.getUid(), unit.getParentGraph( roots ) );
            }
        }

        return map;
    }

    /**
     * Returns a mapping between the uid and the name parent graph of the given
     * organisation units.
     */
    public static Map<String, String> getParentNameGraphMap( List<OrganisationUnit> organisationUnits,
        Collection<OrganisationUnit> roots, boolean includeThis )
    {
        Map<String, String> map = new HashMap<>();

        if ( organisationUnits != null )
        {
            for ( OrganisationUnit unit : organisationUnits )
            {
                map.put( unit.getUid(), unit.getParentNameGraph( roots, includeThis ) );
            }
        }

        return map;
    }

    /**
     * Indicates whether this organisation unit is associated with the given data
     * element through its data set associations.
     */
    public boolean hasDataElement( DataElement dataElement )
    {
        for ( DataSet dataSet : dataSets )
        {
            if ( dataSet.getDataElements().contains( dataElement ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates whether this organisation unit has at least one associated category
     * option.
     */
    public boolean hasCategoryOptions()
    {
        return categoryOptions != null && !categoryOptions.isEmpty();
    }

    public boolean isRoot()
    {
        return parent == null;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public OrganisationUnit getParent()
    {
        return parent;
    }

    public void setParent( OrganisationUnit parent )
    {
        this.parent = parent;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getPath()
    {
        List<String> pathList = new ArrayList<>();
        Set<String> visitedSet = new HashSet<>();
        OrganisationUnit currentParent = parent;

        pathList.add( uid );

        while ( currentParent != null )
        {
            if ( !visitedSet.contains( currentParent.getUid() ) )
            {
                pathList.add( currentParent.getUid() );
                visitedSet.add( currentParent.getUid() );
                currentParent = currentParent.getParent();
            }
            else
            {
                currentParent = null; // Protect against cyclic org unit graphs
            }
        }

        Collections.reverse( pathList );

        path = PATH_SEP + StringUtils.join( pathList, PATH_SEP );

        return path;
    }

    /**
     * Do not set directly, managed by persistence layer.
     */
    public void setPath( String path )
    {
        this.path = path;
    }

    /**
     * Used by persistence layer. Purpose is to have a column for use in database
     * queries. For application use see {@link OrganisationUnit#getLevel()} which
     * has better performance.
     */
    public Integer getHierarchyLevel()
    {
        Set<String> uids = Sets.newHashSet( uid );

        OrganisationUnit current = this;

        while ( (current = current.getParent()) != null )
        {
            boolean add = uids.add( current.getUid() );

            if ( !add )
            {
                break; // Protect against cyclic org unit graphs
            }
        }

        hierarchyLevel = uids.size();

        return hierarchyLevel;
    }

    /**
     * Do not set directly.
     */
    public void setHierarchyLevel( Integer hierarchyLevel )
    {
        this.hierarchyLevel = hierarchyLevel;
    }

    @JsonProperty
    @JsonSerialize( contentUsing = JacksonOrganisationUnitChildrenSerializer.class )
    @JacksonXmlElementWrapper( localName = "children", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "child", namespace = DxfNamespaces.DXF_2_0 )
    public Set<OrganisationUnit> getChildren()
    {
        return children;
    }

    public void setChildren( Set<OrganisationUnit> children )
    {
        this.children = children;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getOpeningDate()
    {
        return openingDate;
    }

    public void setOpeningDate( Date openingDate )
    {
        this.openingDate = openingDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate( Date closedDate )
    {
        this.closedDate = closedDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( PropertyType.URL )
    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getContactPerson()
    {
        return contactPerson;
    }

    public void setContactPerson( String contactPerson )
    {
        this.contactPerson = contactPerson;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getAddress()
    {
        return address;
    }

    public void setAddress( String address )
    {
        this.address = address;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( PropertyType.EMAIL )
    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @Property( PropertyType.PHONENUMBER )
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    @JsonProperty( "organisationUnitGroups" )
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "organisationUnitGroups", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "organisationUnitGroup", namespace = DxfNamespaces.DXF_2_0 )
    public Set<OrganisationUnitGroup> getGroups()
    {
        return groups;
    }

    public void setGroups( Set<OrganisationUnitGroup> groups )
    {
        this.groups = groups;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "dataSets", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "dataSet", namespace = DxfNamespaces.DXF_2_0 )
    public Set<DataSet> getDataSets()
    {
        return dataSets;
    }

    public void setDataSets( Set<DataSet> dataSets )
    {
        this.dataSets = dataSets;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "programs", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "program", namespace = DxfNamespaces.DXF_2_0 )
    public Set<Program> getPrograms()
    {
        return programs;
    }

    public void setPrograms( Set<Program> programs )
    {
        this.programs = programs;
    }

    @JsonProperty
    @JsonSerialize( contentAs = BaseIdentifiableObject.class )
    @JacksonXmlElementWrapper( localName = "users", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "userItem", namespace = DxfNamespaces.DXF_2_0 )
    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers( Set<User> users )
    {
        this.users = users;
    }

    public Set<CategoryOption> getCategoryOptions()
    {
        return categoryOptions;
    }

    public void setCategoryOptions( Set<CategoryOption> categoryOptions )
    {
        this.categoryOptions = categoryOptions;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Geometry getGeometry()
    {
        return geometry;
    }

    public void setGeometry( Geometry geometry )
    {
        this.geometry = geometry;
    }

    @Override
    public FeatureType getFeatureType()
    {
        return geometry != null ? FeatureType.getTypeFromName( this.geometry.getGeometryType() ) : null;
    }

    @Override
    public String getCoordinates()
    {
        return extractCoordinates( this.getGeometry() );
    }

    @Override
    public boolean hasCoordinates()
    {
        return this.geometry != null;
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.ORGANISATION_UNIT;
    }

    // -------------------------------------------------------------------------
    // Getters and setters for transient fields
    // -------------------------------------------------------------------------

    public List<String> getGroupNames()
    {
        return groupNames;
    }

    public void setGroupNames( List<String> groupNames )
    {
        this.groupNames = groupNames;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue( Double value )
    {
        this.value = value;
    }

    public boolean isCurrentParent()
    {
        return currentParent;
    }

    public void setCurrentParent( boolean currentParent )
    {
        this.currentParent = currentParent;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getMemberCount()
    {
        return memberCount;
    }

    public void setMemberCount( Integer memberCount )
    {
        this.memberCount = memberCount;
    }

    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getGeometryAsJson()
    {
        GeometryJSON geometryJSON = new GeometryJSON();

        return this.geometry != null ? geometryJSON.toString( this.geometry ) : null;
    }

    /**
     * Set the Geometry field using a GeoJSON
     * (https://en.wikipedia.org/wiki/GeoJSON) String, like {"type":"Point",
     * "coordinates":[....]}
     *
     * @param geometryAsJsonString String containing a GeoJSON JSON payload
     * @throws IOException an error occurs parsing the payload
     */
    public void setGeometryAsJson( String geometryAsJsonString )
        throws IOException
    {
        if ( !Strings.isNullOrEmpty( geometryAsJsonString ) )
        {
            GeometryJSON geometryJSON = new GeometryJSON();

            Geometry geometry = geometryJSON.read( geometryAsJsonString );

            geometry.setSRID( 4326 );

            this.geometry = geometry;
        }
    }

    public void removeProgram( Program program )
    {
        program.getOrganisationUnits().remove( this );
        this.programs.remove( program );
    }
}
