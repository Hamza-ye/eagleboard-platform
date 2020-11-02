package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.programrule.ProgramRuleVariable;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class ProgramRuleVariableSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "programRuleVariable";

    public static final String PLURAL = "programRuleVariables";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( ProgramRuleVariable.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1600 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_PROGRAM_RULE_MANAGEMENT" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_PROGRAM_RULE_MANAGEMENT" ) ) );

        return schema;
    }
}
