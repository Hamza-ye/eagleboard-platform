package com.mass3d.category;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.common.IdentifiableProperty;

/**
 * CategoryComboMap is used to lookup categoryoptioncombos by identifiers of the
 * categoryoptions. Identifiers must be trimmed of whitespace and be
 * concatenated in the order given by the categories property in object of this
 * class.
 * 
 */
public class CategoryComboMap
{
    private final IdentifiableProperty idScheme;

    private final List<Category> categories;

    private CategoryCombo categoryCombo;

    private Map<String, CategoryOptionCombo> ccMap;

    public List<Category> getCategories()
    {
        return categories;
    }

    public IdentifiableProperty getIdScheme()
    {
        return idScheme;
    }

    public CategoryCombo getCategoryCombo()
    {
        return categoryCombo;
    }

    public class CategoryComboMapException
        extends Exception
    {
        public CategoryComboMapException( String msg )
        {
            super( msg );
        }
    }

    public CategoryComboMap( CategoryCombo cc, IdentifiableProperty idScheme )
        throws CategoryComboMapException
    {
        this.categoryCombo = cc;
        this.idScheme = idScheme;
        ccMap = new HashMap<>();

        categories = categoryCombo.getCategories();

        Collection<CategoryOptionCombo> optionCombos = categoryCombo.getOptionCombos();

        for ( CategoryOptionCombo optionCombo : optionCombos )
        {
            String compositeIdentifier = "";

            for ( Category category : categories )
            {
                CategoryOption catopt = category.getCategoryOption( optionCombo );
                
                if ( catopt == null )
                {
                    throw new CategoryComboMapException( "No categoryOption in " + category.getName() + " matching "
                        + optionCombo.getName() );
                }
                else
                {
                    String identifier;

                    switch ( idScheme )
                    {
                        case UID:
                            identifier = catopt.getUid().trim();
                            break;
                        case CODE:
                            identifier = catopt.getCode().trim();
                            break;
                        case NAME:
                            identifier = catopt.getName().trim();
                            break;
                        default:
                            throw new CategoryComboMapException( "Unsupported ID Scheme: " + idScheme.toString() );
                    }

                    if ( identifier == null )
                    {
                        throw new CategoryComboMapException( "No " + idScheme.toString() + " identifier for CategoryOption: " + catopt.getName() );
                    }

                    compositeIdentifier += "\"" + identifier + "\"";
                }
            }
            
            this.ccMap.put( compositeIdentifier, optionCombo );
        }
    }

    /**
     * Look up the category option combo based on a composite identifier.
     * 
     * @param compositeIdentifier the composite identifier.
     * @return a category option combo.
     */
    public CategoryOptionCombo getCategoryOptionCombo( String compositeIdentifier )
    {
        return ccMap.get( compositeIdentifier );
    }

    /**
     * Create a composite identifier from a list of identifiers
     * 
     * Note: identifiers must be in same order as list of categories in the map.
     * 
     * @param identifiers the list of identifiers.
     * @return a composite identifier.
     */
    public String getKey( List<String> identifiers )
    {
        String key = "";
        
        for ( String identifier : identifiers )
        {
            key += "\"" + identifier + "\"";
        }
        
        return key;
    }
    
    public String toString()
    {
        return "CatComboMap: catcombo=" + categoryCombo.getName() + " map:" + ccMap.toString();
    }
}
