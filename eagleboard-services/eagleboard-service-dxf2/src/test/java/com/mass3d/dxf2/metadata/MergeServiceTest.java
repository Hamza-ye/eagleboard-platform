package com.mass3d.dxf2.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import com.mass3d.DhisSpringTest;
import com.mass3d.common.MergeMode;
import com.mass3d.dxf2.metadata.merge.Simple;
import com.mass3d.dxf2.metadata.merge.SimpleCollection;
import com.mass3d.indicator.Indicator;
import com.mass3d.indicator.IndicatorType;
import com.mass3d.schema.MergeParams;
import com.mass3d.schema.MergeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MergeServiceTest
    extends DhisSpringTest
{
    @Autowired
    private MergeService mergeService;

    @Override
    public void setUpTest()
    {
    }

    @Test
    public void simpleReplace()
    {
        Date date = new Date();
        Simple source = new Simple( "string", 10, date, false, 123, 2.5f );
        Simple target = new Simple();

        mergeService.merge( new MergeParams<>( source, target ).setMergeMode( MergeMode.REPLACE ) );

        assertEquals( "string", target.getString() );
        assertEquals( 10, (int) target.getInteger() );
        assertEquals( date, target.getDate() );
        assertEquals( false, target.getBool() );
        assertEquals( 123, target.getAnInt() );
    }

    @Test
    public void simpleMerge()
    {
        Date date = new Date();
        Simple source = new Simple( null, 10, date, null, 123, 2.5f );
        Simple target = new Simple( "hello", 20, date, true, 123, 2.5f );

        mergeService.merge( new MergeParams<>( source, target ).setMergeMode( MergeMode.MERGE ) );

        assertEquals( "hello", target.getString() );
        assertEquals( 10, (int) target.getInteger() );
        assertEquals( date, target.getDate() );
        assertEquals( true, target.getBool() );
    }

    @Test
    public void simpleCollection()
    {
        Date date = new Date();

        SimpleCollection source = new SimpleCollection( "name" );
        source.getSimples().add( new Simple( "simple", 10, date, false, 123, 2.5f ) );
        source.getSimples().add( new Simple( "simple", 20, date, false, 123, 2.5f ) );
        source.getSimples().add( new Simple( "simple", 30, date, false, 123, 2.5f ) );

        SimpleCollection target = new SimpleCollection( "target" );

        mergeService.merge( new MergeParams<>( source, target ).setMergeMode( MergeMode.MERGE ) );

        assertEquals( "name", target.getName() );
        assertEquals( 3, target.getSimples().size() );

        assertTrue( target.getSimples().contains( source.getSimples().get( 0 ) ) );
        assertTrue( target.getSimples().contains( source.getSimples().get( 1 ) ) );
        assertTrue( target.getSimples().contains( source.getSimples().get( 2 ) ) );
    }

//    @Test
//    public void mergeOrgUnitGroup()
//    {
//        OrganisationUnit organisationUnitA = createOrganisationUnit( 'A' );
//        OrganisationUnit organisationUnitB = createOrganisationUnit( 'B' );
//        OrganisationUnit organisationUnitC = createOrganisationUnit( 'C' );
//        OrganisationUnit organisationUnitD = createOrganisationUnit( 'D' );
//
//        OrganisationUnitGroup organisationUnitGroupA = createOrganisationUnitGroup( 'A' );
//        OrganisationUnitGroup organisationUnitGroupB = createOrganisationUnitGroup( 'B' );
//
//        organisationUnitGroupA.getMembers().add( organisationUnitA );
//        organisationUnitGroupA.getMembers().add( organisationUnitB );
//        organisationUnitGroupA.getMembers().add( organisationUnitC );
//        organisationUnitGroupA.getMembers().add( organisationUnitD );
//
//        OrganisationUnitGroupSet organisationUnitGroupSetA = createOrganisationUnitGroupSet( 'A' );
//        organisationUnitGroupSetA.addOrganisationUnitGroup( organisationUnitGroupA );
//
//        mergeService.merge( new MergeParams<>( organisationUnitGroupA, organisationUnitGroupB ).setMergeMode( MergeMode.REPLACE ) );
//
//        assertFalse( organisationUnitGroupB.getMembers().isEmpty() );
//        assertEquals( 4, organisationUnitGroupB.getMembers().size() );
//        assertNotNull( organisationUnitGroupB.getGroupSets() );
//        assertFalse( organisationUnitGroupB.getGroupSets().isEmpty() );
//    }
//
//    @Test
//    public void mergeOrgUnitGroupSet()
//    {
//        OrganisationUnit organisationUnitA = createOrganisationUnit( 'A' );
//        OrganisationUnit organisationUnitB = createOrganisationUnit( 'B' );
//        OrganisationUnit organisationUnitC = createOrganisationUnit( 'C' );
//        OrganisationUnit organisationUnitD = createOrganisationUnit( 'D' );
//
//        OrganisationUnitGroup organisationUnitGroupA = createOrganisationUnitGroup( 'A' );
//        organisationUnitGroupA.getMembers().add( organisationUnitA );
//        organisationUnitGroupA.getMembers().add( organisationUnitB );
//        organisationUnitGroupA.getMembers().add( organisationUnitC );
//        organisationUnitGroupA.getMembers().add( organisationUnitD );
//
//        OrganisationUnitGroupSet organisationUnitGroupSetA = createOrganisationUnitGroupSet( 'A' );
//        OrganisationUnitGroupSet organisationUnitGroupSetB = createOrganisationUnitGroupSet( 'B' );
//        organisationUnitGroupSetA.addOrganisationUnitGroup( organisationUnitGroupA );
//
//        mergeService.merge( new MergeParams<>( organisationUnitGroupSetA, organisationUnitGroupSetB ).setMergeMode( MergeMode.REPLACE ) );
//
//        assertFalse( organisationUnitGroupSetB.getOrganisationUnitGroups().isEmpty() );
//        assertEquals( organisationUnitGroupSetA.getName(), organisationUnitGroupSetB.getName() );
//        assertEquals( organisationUnitGroupSetA.getDescription(), organisationUnitGroupSetB.getDescription() );
//        assertEquals( organisationUnitGroupSetA.isCompulsory(), organisationUnitGroupSetB.isCompulsory() );
//        assertEquals( organisationUnitGroupSetA.isIncludeSubhierarchyInAnalytics(), organisationUnitGroupSetB.isIncludeSubhierarchyInAnalytics() );
//        assertEquals( 1, organisationUnitGroupSetB.getOrganisationUnitGroups().size() );
//    }

    @Test
    public void testIndicatorClone()
    {
        IndicatorType indicatorType = createIndicatorType( 'A' );
        Indicator indicator = createIndicator( 'A', indicatorType );
        Indicator clone = mergeService.clone( indicator );

        assertEquals( indicator.getName(), clone.getName() );
        assertEquals( indicator.getUid(), clone.getUid() );
        assertEquals( indicator.getCode(), clone.getCode() );
        assertEquals( indicator.getIndicatorType(), clone.getIndicatorType() );
    }
}
