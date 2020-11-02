package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.programrule.ProgramRule;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramRuleSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programRule";

    public static final String PLURAL = "programRules";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramRule.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1620 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists
            .newArrayList( "F_PROGRAM_RULE_ADD", "F_PROGRAM_RULE_MANAGEMENT" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists
            .newArrayList( "F_PROGRAM_RULE_DELETE", "F_PROGRAM_RULE_MANAGEMENT" ) ) );

        return schema;
    }
}
