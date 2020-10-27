package com.mass3d.schema.transformer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import com.mass3d.common.CodeGenerator;
import com.mass3d.schema.AbstractPropertyTransformer;
import com.mass3d.user.User;
import com.mass3d.user.UserCredentials;

public class UserPropertyTransformer
    extends AbstractPropertyTransformer<User>
{
    public UserPropertyTransformer()
    {
        super( UserDto.class );
    }

    @Override
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    public Object transform( Object o )
    {
        if ( !(o instanceof User) )
        {
            if ( o instanceof Collection)
            {
                Collection collection = (Collection) o;

                if ( collection.isEmpty() )
                {
                    return o;
                }

                Object next = collection.iterator().next();

                if ( !(next instanceof User) )
                {
                    return o;
                }

                Collection<UserDto> userDtoCollection = newCollectionInstance( collection.getClass() );
                collection.forEach( user -> userDtoCollection.add( buildUserDto( (User) user ) ) );

                return userDtoCollection;
            }

            return o;
        }

        return buildUserDto( (User) o );
    }

    private UserDto buildUserDto( User user )
    {
        UserCredentials userCredentials = user.getUserCredentials();

        UserDto.UserDtoBuilder builder = UserDto.builder()
            .id( user.getUid() )
            .code( user.getCode() )
            .displayName( user.getDisplayName() );

        if ( userCredentials != null )
        {
            builder.username( userCredentials.getUsername() );
        }

        return builder.build();
    }

    @Data
    @Builder
    public static class UserDto
    {
        private String id;

        private String code;

        private String name;

        private String displayName;

        private String username;

        @JsonProperty
        public String getId()
        {
            return id;
        }

        @JsonProperty
        public String getCode()
        {
            return code;
        }

        @JsonProperty
        public String getName()
        {
            return name;
        }

        @JsonProperty
        public String getDisplayName()
        {
            return displayName;
        }

        @JsonProperty
        public String getUsername()
        {
            return username;
        }
    }

    public static final class JacksonSerialize extends StdSerializer<User>
    {
        public JacksonSerialize()
        {
            super( User.class );
        }

        @Override
        public void serialize( User user, JsonGenerator gen, SerializerProvider provider ) throws IOException
        {
            UserCredentials userCredentials = user.getUserCredentials();

            gen.writeStartObject();
            gen.writeStringField( "id", user.getUid() );
            gen.writeStringField( "code", user.getCode() );
            gen.writeStringField( "name", user.getName() );
            gen.writeStringField( "displayName", user.getDisplayName() );

            if ( userCredentials != null )
            {
                gen.writeStringField( "username", userCredentials.getUsername() );
            }

            gen.writeEndObject();
        }
    }

    public static final class JacksonDeserialize extends StdDeserializer<User>
    {
        public JacksonDeserialize()
        {
            super( User.class );
        }

        @Override
        public User deserialize( JsonParser jp, DeserializationContext ctxt ) throws IOException, JsonProcessingException
        {
            User user = new User();
            UserCredentials userCredentials = new UserCredentials();
            user.setUserCredentials( userCredentials );

            JsonNode node = jp.getCodec().readTree( jp );

            if ( node.has( "id" ) )
            {
                String identifier = node.get( "id" ).asText();

                if ( CodeGenerator.isValidUid( identifier ) )
                {
                    user.setUid( identifier );
                    userCredentials.setUid( identifier );
                }
                else
                {
//                    userCredentials.setUuid( UUID.fromString( identifier ) );
                }
            }

            if ( node.has( "code" ) )
            {
                String code = node.get( "code" ).asText();

                user.setCode( code );
                userCredentials.setCode( code );
            }

            if ( node.has( "username" ) )
            {
                userCredentials.setUsername( node.get( "username" ).asText() );
            }

            return user;
        }
    }

    private static <E> Collection<E> newCollectionInstance( Class<?> clazz )
    {
        if ( List.class.isAssignableFrom( clazz ) )
        {
            return new ArrayList<>();
        }
        else if ( Set.class.isAssignableFrom( clazz ) )
        {
            return new HashSet<>();
        }
        else
        {
            throw new RuntimeException( "Unknown Collection type." );
        }
    }
}
