package com.mass3d.dxf2.metadata.objectbundle.validation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static com.mass3d.importexport.ImportStrategy.CREATE_AND_UPDATE;
import static org.junit.Assert.assertThat;
import static org.mockito.junit.MockitoJUnit.rule;

import com.google.common.collect.ImmutableMap;
import com.mass3d.dataelement.DataElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.commons.collection.ListUtils;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundle;
import com.mass3d.dxf2.metadata.objectbundle.ObjectBundleParams;
import com.mass3d.feedback.TypeReport;
import com.mass3d.preheat.Preheat;
import com.mass3d.preheat.PreheatIdentifier;
import com.mass3d.schema.SchemaService;
import com.mass3d.schema.validation.SchemaValidator;
import com.mass3d.security.acl.AclService;
import com.mass3d.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoRule;

public class ValidationFactoryTest
{
    @Mock
    private SchemaValidator schemaValidator;

    @Mock
    private SchemaService schemaService;

    @Mock
    private AclService aclService;

    @Mock
    private UserService userService;

    @Rule
    public MockitoRule mockitoRule = rule();

    private ValidationFactory validationFactory;

    @Before
    public void setUp()
    {
        // Create a validation factory with a dummy check
        validationFactory = new ValidationFactory( schemaValidator, schemaService, aclService, userService,
            Collections.emptyList(), ImmutableMap.of( CREATE_AND_UPDATE, ListUtils.newList( DummyCheck.class ) ) );
    }

    @Test
    public void verifyValidationFactoryProcessValidationCheck()
    {
        ObjectBundle bundle = createObjectBundle();

        TypeReport typeReport = validationFactory.validateBundle( bundle, DataElement.class,
            bundle.getObjects( DataElement.class, true ), bundle.getObjects( DataElement.class, false ) );

        // verify that object has been removed from bundle
        assertThat( bundle.getObjects( DataElement.class, false ), hasSize( 0 ) );
        assertThat( bundle.getObjects( DataElement.class, true ), hasSize( 0 ) );
        assertThat( typeReport.getStats().getCreated(), is( 0 ) );
        assertThat( typeReport.getStats().getUpdated(), is( 0 ) );
        assertThat( typeReport.getStats().getDeleted(), is( 0 ) );
        assertThat( typeReport.getStats().getIgnored(), is( 1 ) );
        assertThat( typeReport.getObjectReports(), hasSize( 1 ) );
    }

    private ObjectBundle createObjectBundle()
    {

        DataElement attribute1 = new DataElement();
        attribute1.setUid( "u1" );

        ObjectBundleParams objectBundleParams = new ObjectBundleParams();
        Preheat preheat = new Preheat();

        final Map<Class<? extends IdentifiableObject>, List<IdentifiableObject>> objectMap = new HashMap<>();
        objectMap.put( DataElement.class, new ArrayList<>() );

        objectMap.get( DataElement.class ).add( attribute1 );

        preheat.put( PreheatIdentifier.UID, attribute1 );

        return new ObjectBundle( objectBundleParams, preheat, objectMap );
    }

}