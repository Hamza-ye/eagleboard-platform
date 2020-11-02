package com.mass3d.schema.descriptors;

import com.google.common.collect.Lists;
import com.mass3d.dataset.Section;
import com.mass3d.schema.Schema;
import com.mass3d.schema.SchemaDescriptor;
import com.mass3d.security.Authority;
import com.mass3d.security.AuthorityType;

public class SectionSchemaDescriptor implements SchemaDescriptor
{
    public static final String SINGULAR = "section";

    public static final String PLURAL = "sections";

    public static final String API_ENDPOINT = "/" + PLURAL;

    @Override
    public Schema getSchema()
    {
        Schema schema = new Schema( Section.class, SINGULAR, PLURAL );
        schema.setRelativeApiEndpoint( API_ENDPOINT );
        schema.setOrder( 1320 );

        schema.getAuthorities().add( new Authority( AuthorityType.CREATE, Lists.newArrayList( "F_SECTION_ADD" ) ) );
        schema.getAuthorities().add( new Authority( AuthorityType.DELETE, Lists.newArrayList( "F_SECTION_DELETE" ) ) );

        return schema;
    }
}
