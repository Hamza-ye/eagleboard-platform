package com.mass3d.attribute;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import com.mass3d.category.Category;
import com.mass3d.category.CategoryOption;
import com.mass3d.category.CategoryOptionCombo;
import com.mass3d.category.CategoryOptionGroup;
import com.mass3d.category.CategoryOptionGroupSet;
import com.mass3d.common.IdentifiableObjectStore;
import com.mass3d.constant.Constant;
import com.mass3d.dataelement.DataElement;
import com.mass3d.dataelement.DataElementGroup;
import com.mass3d.dataelement.DataElementGroupSet;
import com.mass3d.dataset.DataSet;
import com.mass3d.document.Document;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorGroup;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.organisationunit.OrganisationUnitGroup;
import com.mass3d.organisationunit.OrganisationUnitGroupSet;
import com.mass3d.program.Program;
import com.mass3d.program.ProgramIndicator;
import com.mass3d.program.ProgramStage;
import com.mass3d.user.User;
import com.mass3d.user.UserGroup;
import com.mass3d.validation.ValidationRule;
import com.mass3d.validation.ValidationRuleGroup;

public interface AttributeStore
    extends IdentifiableObjectStore<Attribute>
{
    String ID = AttributeStore.class.getName();

    ImmutableMap<Class<?>, String> CLASS_ATTRIBUTE_MAP = ImmutableMap.<Class<?>, String>builder()
        .put( DataElement.class, "dataElementAttribute" )
        .put( DataElementGroup.class, "dataElementGroupAttribute" )
        .put( Indicator.class, "indicatorAttribute" )
        .put( IndicatorGroup.class, "indicatorGroupAttribute" )
        .put( DataSet.class, "dataSetAttribute" )
        .put( OrganisationUnit.class, "organisationUnitAttribute" )
        .put( OrganisationUnitGroup.class, "organisationUnitGroupAttribute" )
        .put( OrganisationUnitGroupSet.class, "organisationUnitGroupSetAttribute" )
        .put( User.class, "userAttribute" )
        .put( UserGroup.class, "userGroupAttribute" )
        .put( Program.class, "programAttribute" )
        .put( ProgramStage.class, "programStageAttribute" )
//        .put( TrackedEntityType.class, "trackedEntityTypeAttribute" )
//        .put( TrackedEntityAttribute.class, "trackedEntityAttributeAttribute" )
        .put( CategoryOption.class, "categoryOptionAttribute" )
        .put( CategoryOptionGroup.class, "categoryOptionGroupAttribute" )
        .put( Document.class, "documentAttribute" )
        .put( Option.class, "optionAttribute" )
        .put( OptionSet.class, "optionSetAttribute" )
        .put( Constant.class, "constantAttribute" )
//        .put( LegendSet.class, "legendSetAttribute" )
        .put( ProgramIndicator.class, "programIndicatorAttribute" )
//        .put( SqlView.class, "sqlViewAttribute" )
//        .put( Section.class, "sectionAttribute" )
        .put( CategoryOptionCombo.class, "categoryOptionComboAttribute" )
        .put( CategoryOptionGroupSet.class, "categoryOptionGroupSetAttribute" )
        .put( DataElementGroupSet.class, "dataElementGroupSetAttribute" )
        .put( ValidationRule.class, "validationRuleAttribute" )
        .put( ValidationRuleGroup.class, "validationRuleGroupAttribute" )
        .put( Category.class, "categoryAttribute" )
        .build();

    /**
     * Get all metadata attributes for a given class, returns empty list for un-supported types.
     *
     * @param klass Class to get metadata attributes for
     * @return List of attributes for this class
     */
    List<Attribute> getAttributes(Class<?> klass);

    /**
     * Get all mandatory metadata attributes for a given class, returns empty list for un-supported types.
     *
     * @param klass Class to get metadata attributes for
     * @return List of mandatory metadata attributes for this class
     */
    List<Attribute> getMandatoryAttributes(Class<?> klass);

    /**
     * Get all unique metadata attributes for a given class, returns empty list for un-supported types.
     *
     * @param klass Class to get metadata attributes for
     * @return List of unique metadata attributes for this class
     */
    List<Attribute> getUniqueAttributes(Class<?> klass);
}
