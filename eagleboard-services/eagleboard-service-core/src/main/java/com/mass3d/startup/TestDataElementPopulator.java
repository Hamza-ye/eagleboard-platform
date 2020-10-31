package com.mass3d.startup;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import com.mass3d.analytics.AggregationType;
import com.mass3d.category.CategoryCombo;
import com.mass3d.category.CategoryService;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementDomain;
import com.mass3d.dataelement.DataElementService;
import com.mass3d.system.startup.TransactionContextStartupRoutine;
import com.mass3d.user.User;
import com.mass3d.user.UserAuthorityGroup;
import com.mass3d.user.UserCredentials;
import com.mass3d.user.UserService;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDataElementPopulator
    extends TransactionContextStartupRoutine
{
    private final DataElementService dataElementService;
    private final CategoryService categoryService;

    public TestDataElementPopulator( DataElementService dataElementService, CategoryService categoryService )
    {
        checkNotNull( dataElementService );
        checkNotNull( categoryService );
        this.dataElementService = dataElementService;
        this.categoryService = categoryService;
    }

    @Override
    public void executeInTransaction()
    {
        DataElement dataElementA = createDataElement( 'A' );
        DataElement dataElementB = createDataElement( 'B' );
        DataElement dataElementC = createDataElement( 'C' );

        dataElementService.addDataElement(dataElementA);
        Long idA = dataElementA.getId();
        dataElementService.addDataElement(dataElementB);
        Long idB = dataElementB.getId();
        dataElementService.addDataElement(dataElementC);
        Long idC = dataElementC.getId();
    }

    public DataElement createDataElement( char uniqueCharacter )
    {
        return createDataElement( uniqueCharacter, null );
    }

    /**
     * @param uniqueCharacter A unique character to identify the object.
     * @param categoryCombo   The category combo.
     */
    public DataElement createDataElement( char uniqueCharacter, CategoryCombo categoryCombo )
    {
        DataElement dataElement = new DataElement();
        dataElement.setAutoFields();

        dataElement.setUid( "deabcdefgh" + uniqueCharacter );
        dataElement.setName( "DataElement" + uniqueCharacter );
        dataElement.setShortName( "DataElementShort" + uniqueCharacter );
        dataElement.setCode( "DataElementCode" + uniqueCharacter );
        dataElement.setDescription( "DataElementDescription" + uniqueCharacter );
        dataElement.setValueType( ValueType.INTEGER );
        dataElement.setDomainType( DataElementDomain.AGGREGATE );
        dataElement.setAggregationType( AggregationType.SUM );
        dataElement.setZeroIsSignificant( false );

        if ( categoryCombo != null )
        {
            dataElement.setCategoryCombo( categoryCombo );
        }
        else //if ( categoryService != null )
        {
            dataElement.setCategoryCombo( categoryService.getDefaultCategoryCombo() );
        }

        return dataElement;
    }

}
