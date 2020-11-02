package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.validation.ValidationRule;

public class ValidationRuleSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "validationRule";

    public static final String PLURAL = "validationRules";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ValidationRule.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1390 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PUBLIC, Lists.newArrayList( "F_VALIDATIONRULE_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.CREATE_PRIVATE, Lists.newArrayList( "F_VALIDATIONRULE_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_VALIDATIONRULE_DELETE" ) ) );

        return schema;
    }
}
