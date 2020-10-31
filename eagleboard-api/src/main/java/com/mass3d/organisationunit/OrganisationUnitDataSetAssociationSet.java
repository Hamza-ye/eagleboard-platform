package com.mass3d.organisationunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrganisationUnitDataSetAssociationSet
{
    /**
     * List of data set association sets.
     */
    private List<Set<String>> dataSetAssociationSets = new ArrayList<>();

    /**
     * Mapping between organisation unit identifier and index of association set in list.
     */
    private Map<String, Integer> organisationUnitAssociationSetMap = new HashMap<>();

    /**
     * Set of distinct data sets in all association sets.
     */
    private Set<String> distinctDataSets = new HashSet<>();

    /**
     * Map contains authorities for each DataSet for current user
     */
    private Map<String, String> dataSetAuthMap = new HashMap<>();

    public OrganisationUnitDataSetAssociationSet()
    {
    }

    public List<Set<String>> getDataSetAssociationSets()
    {
        return dataSetAssociationSets;
    }

    public void setDataSetAssociationSets( List<Set<String>> dataSetAssociationSets )
    {
        this.dataSetAssociationSets = dataSetAssociationSets;
    }

    public Map<String, Integer> getOrganisationUnitAssociationSetMap()
    {
        return organisationUnitAssociationSetMap;
    }

    public void setOrganisationUnitAssociationSetMap( Map<String, Integer> organisationUnitAssociationSetMap )
    {
        this.organisationUnitAssociationSetMap = organisationUnitAssociationSetMap;
    }

    public Set<String> getDistinctDataSets()
    {
        return distinctDataSets;
    }

    public void setDistinctDataSets( Set<String> distinctDataSets )
    {
        this.distinctDataSets = distinctDataSets;
    }

    public Map<String, String> getDataSetAuthMap()
    {
        return this.dataSetAuthMap;
    }

    public void setDataSetAuthMap( Map<String, String> dataSetAuthMap )
    {
        this.dataSetAuthMap = dataSetAuthMap;
    }
}
