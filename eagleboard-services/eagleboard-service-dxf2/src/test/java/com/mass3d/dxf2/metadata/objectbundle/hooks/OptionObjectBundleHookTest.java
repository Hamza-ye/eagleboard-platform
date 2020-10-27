package com.mass3d.dxf2.metadata.objectbundle.hooks;

import java.util.Collections;
import java.util.List;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundleParams;
import com.mass3d.feedback.ErrorCode;
import com.mass3d.feedback.ErrorReport;
import com.mass3d.option.Option;
import com.mass3d.option.OptionSet;
import com.mass3d.preheat.Preheat;
import com.mass3d.preheat.PreheatIdentifier;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test of {@link OptionObjectBundleHook}.
 *
 */
public class OptionObjectBundleHookTest
{
    private OptionObjectBundleHook hook = new OptionObjectBundleHook();

    private Preheat preheat = new Preheat();

    @Test
    public void preCreate()
    {
        OptionSet optionSet = new OptionSet();
        optionSet.setUid( "jadhjSHdhs" );
        Option option = new Option();
        option.setOptionSet( optionSet );

        OptionSet persistedOptionSet = new OptionSet();
        persistedOptionSet.setUid( "jadhjSHdhs" );
        preheat.put( PreheatIdentifier.UID, persistedOptionSet );

        ObjectBundleParams objectBundleParams = new ObjectBundleParams();
        objectBundleParams.setPreheatIdentifier( PreheatIdentifier.UID );
        ObjectBundle bundle = new ObjectBundle( objectBundleParams, preheat, Collections.emptyMap() );
        hook.preCreate( option, bundle );

        Assert.assertEquals( 1, persistedOptionSet.getOptions().size() );
        Assert.assertSame( option, persistedOptionSet.getOptions().get( 0 ) );
    }

    @Test
    public void preCreateOptionSetAvailable()
    {
        OptionSet optionSet = new OptionSet();
        optionSet.setUid( "jadhjSHdhs" );
        Option option = new Option();
        option.setOptionSet( optionSet );

        OptionSet persistedOptionSet = new OptionSet();
        persistedOptionSet.setUid( "jadhjSHdhs" );
        preheat.put( PreheatIdentifier.UID, persistedOptionSet );

        ObjectBundleParams objectBundleParams = new ObjectBundleParams();
        objectBundleParams.setPreheatIdentifier( PreheatIdentifier.UID );
        ObjectBundle bundle = new ObjectBundle( objectBundleParams, preheat, Collections
            .singletonMap( OptionSet.class, Collections.singletonList( optionSet ) ) );
        hook.preCreate( option, bundle );

        Assert.assertEquals( 0, persistedOptionSet.getOptions().size() );
    }

    @Test
    public void validate()
    {
        OptionSet optionSet = new OptionSet();
        optionSet.setUid( "optionSet1" );

        Option option = new Option();
        option.setName( "optionName" );
        option.setCode( "optionCode" );
        optionSet.addOption( option );

        OptionSet persistedOptionSet = new OptionSet();
        persistedOptionSet.setUid( "optionSet1" );

        Option persistedOption = new Option();
        persistedOption.setName( "optionName" );
        persistedOption.setCode( "optionCode" );
        persistedOptionSet.addOption( persistedOption );

        preheat.put( PreheatIdentifier.UID, persistedOptionSet );

        ObjectBundleParams objectBundleParams = new ObjectBundleParams();
        objectBundleParams.setPreheatIdentifier( PreheatIdentifier.UID );
        ObjectBundle bundle = new ObjectBundle( objectBundleParams, preheat,
            Collections.singletonMap( OptionSet.class, Collections.singletonList( persistedOptionSet ) ) );

        List<ErrorReport> errors = hook.validate( option, bundle );

        Assert.assertNotNull( errors );
        Assert.assertEquals( 1, errors.size() );
        Assert.assertEquals( ErrorCode.E4028, errors.get( 0 ).getErrorCode() );
    }
}