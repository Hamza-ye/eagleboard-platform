package com.mass3d.schema.transformers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.mass3d.DhisSpringTest;
import com.mass3d.fieldfilter.FieldFilterParams;
import com.mass3d.fieldfilter.FieldFilterService;
import com.mass3d.node.NodeService;
import com.mass3d.node.NodeUtils;
import com.mass3d.node.types.ComplexNode;
import com.mass3d.node.types.RootNode;
import com.mass3d.schema.annotation.PropertyTransformer;
import com.mass3d.schema.transformer.UserPropertyTransformer;
import com.mass3d.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserPropertyTransformerTest
    extends DhisSpringTest
{
    private static final UUID uuid = UUID.fromString( "6507f586-f154-4ec1-a25e-d7aa51de5216" );

    @Autowired
    @Qualifier( "jsonMapper" )
    private ObjectMapper jsonMapper;

    @Autowired
    @Qualifier( "xmlMapper" )
    private ObjectMapper xmlMapper;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private FieldFilterService fieldFilterService;

    @Test
    public void testNodeServiceSerializer() throws JsonProcessingException
    {
        Simple simple = new Simple( 1, "Simple1" );
        simple.setUser( createUser( 'a' ) );
//        simple.getUser().getUserCredentials().setUuid( uuid );

        ComplexNode complexNode = nodeService.toNode( simple );
        RootNode rootNode = NodeUtils.createRootNode( complexNode );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        nodeService.serialize( rootNode, "application/json", outputStream );
        String jsonSource = outputStream.toString();
        verifyJsonSource( jsonSource );

        Simple simpleFromJson = jsonMapper.readValue( jsonSource, Simple.class );

        assertEquals( 1, simpleFromJson.getId() );
        assertEquals( "Simple1", simpleFromJson.getName() );

        assertNotNull( simple.getUser() );
        assertNotNull( simple.getUser().getUserCredentials() );

        assertEquals( "usernamea", simple.getUser().getUserCredentials().getUsername() );
//        assertEquals( uuid, simple.getUser().getUserCredentials().getUuid() );
    }

    @Test
    public void testFieldNodeServiceSerializer() throws JsonProcessingException
    {
        Simple simple = new Simple( 1, "Simple1" );
        simple.setUser( createUser( 'a' ) );
//        simple.getUser().getUserCredentials().setUuid( uuid );

        simple.getUsers().add( createUser( 'A' ) );
        simple.getUsers().add( createUser( 'B' ) );
        simple.getUsers().add( createUser( 'C' ) );
        simple.getUsers().add( createUser( 'D' ) );

        ComplexNode complexNode = nodeService.toNode( simple );
        RootNode rootNode = NodeUtils.createRootNode( complexNode );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        nodeService.serialize( rootNode, "application/json", outputStream );
        String jsonSource = outputStream.toString();
        verifyJsonSource( jsonSource );

        Simple simpleFromJson = jsonMapper.readValue( jsonSource, Simple.class );

        assertEquals( 1, simpleFromJson.getId() );
        assertEquals( "Simple1", simpleFromJson.getName() );

        assertNotNull( simple.getUser() );
        assertNotNull( simple.getUser().getUserCredentials() );

        assertEquals( "usernamea", simple.getUser().getUserCredentials().getUsername() );
//        assertEquals( uuid, simple.getUser().getUserCredentials().getUuid() );

        assertNotNull( simple.getUsers() );
        assertEquals( 4, simple.getUsers().size() );

        FieldFilterParams params = new FieldFilterParams(
            Collections.singletonList( simple ), Collections.singletonList( "id,name,user[id,code],users[id,code]" ) );

        ComplexNode node = fieldFilterService.toComplexNode( params );
    }

    @Test
    public void testFieldNodeServiceSerializerPresetStar() throws JsonProcessingException
    {
        Simple simple = new Simple( 1, "Simple1" );
        simple.setUser( createUser( 'a' ) );
//        simple.getUser().getUserCredentials().setUuid( uuid );

        simple.getUsers().add( createUser( 'A' ) );
        simple.getUsers().add( createUser( 'B' ) );
        simple.getUsers().add( createUser( 'C' ) );
        simple.getUsers().add( createUser( 'D' ) );

        ComplexNode complexNode = nodeService.toNode( simple );
        RootNode rootNode = NodeUtils.createRootNode( complexNode );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        nodeService.serialize( rootNode, "application/json", outputStream );
        String jsonSource = outputStream.toString();
        verifyJsonSource( jsonSource );

        Simple simpleFromJson = jsonMapper.readValue( jsonSource, Simple.class );

        assertEquals( 1, simpleFromJson.getId() );
        assertEquals( "Simple1", simpleFromJson.getName() );

        assertNotNull( simple.getUser() );
        assertNotNull( simple.getUser().getUserCredentials() );

        assertEquals( "usernamea", simple.getUser().getUserCredentials().getUsername() );
//        assertEquals( uuid, simple.getUser().getUserCredentials().getUuid() );

        assertNotNull( simple.getUsers() );
        assertEquals( 4, simple.getUsers().size() );

        FieldFilterParams params = new FieldFilterParams(
            Collections.singletonList( simple ), Collections.singletonList( "id,name,user[*],users[*]" ) );

        ComplexNode node = fieldFilterService.toComplexNode( params );
    }

    @Test
    public void testJsonSerializer() throws JsonProcessingException
    {
        Simple simple = new Simple( 1, "Simple1" );

        User user = createUser( 'a' );

        simple.setUser( user );
//        simple.getUser().getUserCredentials().setUuid( uuid );

        String jsonSource = jsonMapper.writeValueAsString( simple );
        verifyJsonSource( jsonSource );

        Simple simpleFromJson = jsonMapper.readValue( jsonSource, Simple.class );

        assertEquals( 1, simpleFromJson.getId() );
        assertEquals( "Simple1", simpleFromJson.getName() );

        assertNotNull( simple.getUser() );
        assertNotNull( simple.getUser().getUserCredentials() );

        assertEquals( "usernamea", simple.getUser().getUserCredentials().getUsername() );
        assertEquals( user.getUid(), simple.getUser().getUid() );
        // assertEquals( uuid, simple.getUser().getUserCredentials().getUuid() );
    }

    @Test
    public void testXmlSerializer() throws JsonProcessingException
    {
        Simple simple = new Simple( 1, "Simple1" );

        User user = createUser( 'a' );

        simple.setUser( user );
//        simple.getUser().getUserCredentials().setUuid( uuid );

        String xmlSource = xmlMapper.writeValueAsString( simple );
        verifyXmlSource( xmlSource );

        Simple simpleFromJson = xmlMapper.readValue( xmlSource, Simple.class );

        assertEquals( 1, simpleFromJson.getId() );
        assertEquals( "Simple1", simpleFromJson.getName() );

        assertNotNull( simple.getUser() );
        assertNotNull( simple.getUser().getUserCredentials() );

        assertEquals( "usernamea", simple.getUser().getUserCredentials().getUsername() );
        assertEquals( user.getUid(), simple.getUser().getUid() );
        // assertEquals( uuid, simple.getUser().getUserCredentials().getUuid() );
    }

    private void verifyJsonSource( String jsonSource ) throws JsonProcessingException
    {
        JsonNode root = jsonMapper.readTree( jsonSource );
        verifyJsonNode( root );
    }

    private void verifyXmlSource( String xmlSource ) throws JsonProcessingException
    {
        JsonNode root = xmlMapper.readTree( xmlSource );
        verifyJsonNode( root );
    }

    private void verifyJsonNode( JsonNode root )
    {
        assertTrue( root.has( "id" ) );
        assertTrue( root.has( "name" ) );
        assertTrue( root.has( "user" ) );

        JsonNode userNode = root.get( "user" );

        assertTrue( userNode.has( "id" ) );
        assertTrue( userNode.has( "username" ) );

        // assertEquals( userNode.get( "id" ).textValue(), uuid.toString() );
        assertEquals( userNode.get( "id" ).textValue(), "userabcdefa" );
        assertEquals( userNode.get( "username" ).textValue(), "usernamea" );
    }

    @JacksonXmlRootElement( localName = "simple" )
    public static class Simple
    {
        private int id;
        private String name;
        private User user;
        private List<User> users = new ArrayList<>();

        public Simple()
        {
        }

        public Simple( int id, String name )
        {
            this.id = id;
            this.name = name;
        }

        @JsonProperty
        public int getId()
        {
            return id;
        }


        @JsonProperty
        public String getName()
        {
            return name;
        }

        @JsonProperty
        @JsonSerialize( using = UserPropertyTransformer.JacksonSerialize.class )
        @JsonDeserialize( using = UserPropertyTransformer.JacksonDeserialize.class )
        @PropertyTransformer( UserPropertyTransformer.class )
        public User getUser()
        {
            return user;
        }

        public void setUser( User user )
        {
            this.user = user;
        }

        @JsonProperty
        @JsonSerialize( contentUsing = UserPropertyTransformer.JacksonSerialize.class )
        @JsonDeserialize( contentUsing = UserPropertyTransformer.JacksonDeserialize.class )
        @PropertyTransformer( UserPropertyTransformer.class )
        public List<User> getUsers()
        {
            return users;
        }

        public void setUsers( List<User> users )
        {
            this.users = users;
        }
    }
}
