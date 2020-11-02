package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.programrule.ProgramRuleAction;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramRuleActionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programRuleAction";

    public static final String PLURAL = "programRuleActions";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramRuleAction.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1610 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_PROGRAM_RULE_MANAGEMENT" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAM_RULE_MANAGEMENT" ) ) );

        return schema;
    }
}
