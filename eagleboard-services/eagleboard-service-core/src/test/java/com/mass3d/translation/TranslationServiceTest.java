package com.mass3d.translation;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.IdentifiableObjectManager;
import com.mass3d.common.UserContext;
import com.mass3d.common.ValueType;
import com.mass3d.dataelement.DataElement;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import com.mass3d.organisationunit.OrganisationUnit;
import com.mass3d.program.ProgramSection;
import com.mass3d.program.ProgramStage;
import com.mass3d.program.ProgramStageSection;
import com.mass3d.relationship.Relationship;
import com.mass3d.relationship.RelationshipItem;
import com.mass3d.relationship.RelationshipType;
import com.mass3d.trackedentity.TrackedEntityAttribute;
import com.mass3d.trackedentity.TrackedEntityInstance;
import com.mass3d.user.User;
import com.mass3d.user.UserService;
import com.mass3d.user.UserSettingKey;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TranslationServiceTest
    extends DhisSpringTest
{
    @Autowired
    private UserService injectUserService;

    @Autowired
    private IdentifiableObjectManager manager;

    private User user;

    private Locale locale;

    @Override
    public void setUpTest()
    {
        this.userService = injectUserService;
        user = createUserAndInjectSecurityContext( true );

        locale = Locale.FRENCH;
        UserContext.setUser( user );
        UserContext.setUserSetting( UserSettingKey.DB_LOCALE, locale );
    }

    @Test
    public void testOK()
    {
        DataElement dataElementA = createDataElement( 'A' );
        manager.save( dataElementA );

        String translatedValue = "translated";

        Set<Translation> listObjectTranslation = new HashSet<>( dataElementA.getTranslations() );

        listObjectTranslation.add( new Translation( locale.getLanguage(), TranslationProperty.NAME, translatedValue ) );

        manager.updateTranslations( dataElementA, listObjectTranslation );

        assertEquals( translatedValue, dataElementA.getDisplayName() );
    }

    @Test
    public void testFormNameTranslationForOption()
    {
        OptionSet optionSet = createOptionSet( 'A' );
        optionSet.setValueType( ValueType.TEXT );
        manager.save( optionSet );
        Option option = createOption( 'A' );
        option.setOptionSet( optionSet );
        manager.save( option );
        Set<Translation> listObjectTranslation = new HashSet<>( option.getTranslations() );

        String translatedValue = "Option FormName Translated";

        listObjectTranslation.add( new Translation( locale.getLanguage(), TranslationProperty.FORM_NAME, translatedValue ) );

        manager.updateTranslations( option, listObjectTranslation );

        assertEquals( translatedValue, option.getDisplayFormName() );
    }

    @Test
    public void testFormNameTranslationForRelationShip()
    {
        RelationshipType relationshipType = createRelationshipType( 'A' );
        OrganisationUnit organisationUnit = createOrganisationUnit( 'A' );
        TrackedEntityAttribute attribute = createTrackedEntityAttribute( 'A' );
        manager.save( relationshipType );
        manager.save( organisationUnit );
        manager.save( attribute );

        TrackedEntityInstance trackedEntityInstance = createTrackedEntityInstance( 'A', organisationUnit, attribute );
        manager.save( trackedEntityInstance );

        Relationship relationship = new Relationship();
        RelationshipItem from = new RelationshipItem();
        from.setTrackedEntityInstance( trackedEntityInstance );
        RelationshipItem to = new RelationshipItem();
        to.setTrackedEntityInstance( trackedEntityInstance );
        relationship.setFrom( from );
        relationship.setTo( to );
        relationship.setRelationshipType( relationshipType );

        manager.save( relationship );

        String translatedValue = "RelationShip FormName Translated";

        Set<Translation> listObjectTranslation = new HashSet<>( relationship.getTranslations() );

        listObjectTranslation.add( new Translation( locale.getLanguage(), TranslationProperty.FORM_NAME, translatedValue ) );

        manager.updateTranslations( relationship, listObjectTranslation );

        assertEquals( translatedValue, relationship.getDisplayFormName() );
    }

    @Test
    public void testFormNameTranslationForProgramStageSection()
    {
        ProgramStageSection programStageSection = createProgramStageSection( 'A' , 0 );
        manager.save( programStageSection );

        String translatedValue = "ProgramStageSection FormName Translated";

        Set<Translation> listObjectTranslation = new HashSet<>( programStageSection.getTranslations() );

        listObjectTranslation.add( new Translation( locale.getLanguage(), TranslationProperty.FORM_NAME, translatedValue ) );

        manager.updateTranslations( programStageSection, listObjectTranslation );

        assertEquals( translatedValue, programStageSection.getDisplayFormName() );
    }

    @Test
    public void testFormNameTranslationForProgramStage()
    {
        ProgramStage programStage = createProgramStage( 'A', 0 );
        manager.save( programStage );

        String translatedValue = "ProgramStage FormName Translated";

        Set<Translation> listObjectTranslation = new HashSet<>( programStage.getTranslations() );

        listObjectTranslation.add( new Translation( locale.getLanguage(), TranslationProperty.FORM_NAME, translatedValue ) );

        manager.updateTranslations( programStage, listObjectTranslation );

        assertEquals( translatedValue, programStage.getDisplayFormName() );
    }

    @Test
    public void testFormNameTranslationForProgramSection()
    {
        ProgramSection programSection = new ProgramSection();
        programSection.setName( "Section A" );
        programSection.setAutoFields();
        programSection.setSortOrder( 0 );

        manager.save( programSection );

        String translatedValue = "ProgramSection FormName Translated";

        Set<Translation> listObjectTranslation = new HashSet<>( programSection.getTranslations() );

        listObjectTranslation.add( new Translation( locale.getLanguage(), TranslationProperty.FORM_NAME, translatedValue ) );

        manager.updateTranslations( programSection, listObjectTranslation );

        assertEquals( translatedValue, programSection.getDisplayFormName() );
    }

    @Test
    public void testRelationshipTypeFromAndToTranslation()
    {
        RelationshipType relationshipType = createRelationshipType( 'A' );

        relationshipType.setFromToName( "From to name" );
        relationshipType.setToFromName( "To from name" );

        manager.save( relationshipType );

        String fromToNameTranslated = "From to name translated";

        String toFromNameTranslated = "To from name translated";

        Set<Translation> translations = new HashSet<>();
        translations.add(  new Translation( locale.getLanguage(), TranslationProperty.RELATIONSHIP_TO_FROM_NAME, toFromNameTranslated ) );
        translations.add(  new Translation( locale.getLanguage(), TranslationProperty.RELATIONSHIP_FROM_TO_NAME, fromToNameTranslated ) );

        manager.updateTranslations( relationshipType, translations );

        assertEquals( fromToNameTranslated, relationshipType.getDisplayFromToName() );
        assertEquals( toFromNameTranslated, relationshipType.getDisplayToFromName() );
    }
}
