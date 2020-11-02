package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;
import com.mass3d.validation.ValidationRuleGroup;

public class ValidationRuleGroupSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "validationRuleGroup";

    public static final String PLURAL = "validationRuleGroups";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ValidationRuleGroup.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1400 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_VALIDATIONRULEGROUP_PUBLIC_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.UPDATE, Lists.newArrayList( "F_VALIDATIONRULEGROUP_PRIVATE_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_VALIDATIONRULEGROUP_DELETE" ) ) );

        return schema;
    }
}
