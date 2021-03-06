package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.attribute.Attribute;
import com.mass3d.attribute.AttributeValue;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.audit.AuditAttribute;
import com.mass3d.common.annotation.Description;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import com.mass3d.schema.annotation.Property.Value;
import com.mass3d.schema.annotation.PropertyRange;
import com.mass3d.schema.annotation.PropertyTransformer;
import com.mass3d.schema.transformer.UserPropertyTransformer;
import com.mass3d.security.acl.Access;
import com.mass3d.translation.Translation;
import com.mass3d.translation.TranslationProperty;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroupAccess;
import com.mass3d.user.UserSettingKey;

@JacksonXmlRootElement( localName = "identifiableObject", namespace = DxfNamespaces.DXF_2_0 )
public class BaseIdentifiableObject
    extends BaseLinkableObject implements IdentifiableObject
{
    /**
     * The database internal identifier for this Object.
     */
    protected long id;

    /**
     * The Unique Identifier for this Object.
     */
    @AuditAttribute
    protected String uid;

    /**
     * The unique code for this Object.
     */
    @AuditAttribute
    protected String code;

    /**
     * The name of this Object. Required and unique.
     */
    protected String name;

    /**
     * The date this object was created.
     */
    protected Date created;

    /**
     * The date this object was last updated.
     */
    protected Date lastUpdated;

    /**
     * Set of the dynamic attributes values that belong to this data element.
     */
    @AuditAttribute
    protected Set<AttributeValue> attributeValues = new HashSet<>();

    /**
     * Cache of attribute values which allows for lookup by attribute identifier.
     */
    protected Map<String, AttributeValue> cacheAttributeValues = new HashMap<>();

    /**
     * Set of available object translation, normally filtered by locale.
     */
    protected Set<Translation> translations = new HashSet<>();

    /**
     * Cache for object translations, where the cache key is a combination of
     * locale and translation property, and value is the translated value.
     */
    protected Map<String, String> translationCache = new HashMap<>();

    /**
     * This object is available as external read-only.
     */
    protected boolean externalAccess;

    /**
     * Access string for public access.
     */
    protected String publicAccess;

    /**
     * Owner of this object.
     */
    protected User user;

    /**
     * Access for user groups.
     */
    protected Set<UserGroupAccess> userGroupAccesses = new HashSet<>();

    /**
     * Access for users.
     */
    protected Set<UserAccess> userAccesses = new HashSet<>();

    /**
     * Access information for this object. Applies to current user.
     */
    protected transient Access access;

    /**
     * Users who have marked this object as a favorite.
     */
    protected Set<String> favorites = new HashSet<>();

    /**
     * The i18n variant of the name. Not persisted.
     */
    protected transient String displayName;

    /**
     * Last user updated this object.
     */
    protected User lastUpdatedBy;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseIdentifiableObject()
    {
    }

    public BaseIdentifiableObject( long id, String uid, String name )
    {
        this.id = id;
        this.uid = uid;
        this.name = name;
    }

    public BaseIdentifiableObject( String uid, String code, String name )
    {
        this.uid = uid;
        this.code = code;
        this.name = name;
    }

    public BaseIdentifiableObject( IdentifiableObject identifiableObject )
    {
        this.id = identifiableObject.getId();
        this.uid = identifiableObject.getUid();
        this.name = identifiableObject.getName();
        this.created = identifiableObject.getCreated();
        this.lastUpdated = identifiableObject.getLastUpdated();
    }

    // -------------------------------------------------------------------------
    // Comparable implementation
    // -------------------------------------------------------------------------

    /**
     * Compares objects based on display name. A null display name is ordered
     * after a non-null display name.
     */
    @Override
    public int compareTo( IdentifiableObject object )
    {
        if ( this.getDisplayName() == null )
        {
            return object.getDisplayName() == null ? 0 : 1;
        }

        return object.getDisplayName() == null ? -1 :
            this.getDisplayName().compareToIgnoreCase( object.getDisplayName() );
    }

    // -------------------------------------------------------------------------
    // Setters and getters
    // -------------------------------------------------------------------------

    @Override
    @JsonIgnore
    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    @Override
    @JsonProperty( value = "id" )
    @JacksonXmlProperty( localName = "id", isAttribute = true )
    @Description( "The Unique Identifier for this Object." )
    @Property( value = PropertyType.IDENTIFIER, required = Value.FALSE )
    @PropertyRange( min = 11, max = 11 )
    public String getUid()
    {
        return uid;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    @Description( "The unique code for this Object." )
    @Property( PropertyType.IDENTIFIER )
    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    @Description( "The name of this Object. Required and unique." )
    @PropertyRange( min = 1 )
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getDisplayName()
    {
        displayName = getTranslation( TranslationProperty.NAME, displayName );
        return displayName != null ? displayName : getName();
    }

    @JsonIgnore
    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    @Description( "The date this object was created." )
    @Property( value = PropertyType.DATE, required = Value.FALSE )
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @Override
    @JsonProperty
    @JsonSerialize( using = UserPropertyTransformer.JacksonSerialize.class )
    @JsonDeserialize( using = UserPropertyTransformer.JacksonDeserialize.class )
    @PropertyTransformer( UserPropertyTransformer.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public User getLastUpdatedBy()
    {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy( User lastUpdatedBy )
    {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( isAttribute = true )
    @Description( "The date this object was last updated." )
    @Property( value = PropertyType.DATE, required = Value.FALSE )
    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( Date lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    @Override
    @JsonProperty( "attributeValues" )
    @JacksonXmlElementWrapper( localName = "attributeValues", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "attributeValue", namespace = DxfNamespaces.DXF_2_0 )
    public Set<AttributeValue> getAttributeValues()
    {
        return attributeValues;
    }

    @Override
    public void setAttributeValues( Set<AttributeValue> attributeValues )
    {
        cacheAttributeValues.clear();
        this.attributeValues = attributeValues;
    }

    public AttributeValue getAttributeValue( Attribute attribute )
    {
        loadAttributeValuesCacheIfEmpty();
        return cacheAttributeValues.get( attribute.getUid() );
    }

    public AttributeValue getAttributeValue( String attributeUid )
    {
        loadAttributeValuesCacheIfEmpty();
        return cacheAttributeValues.get( attributeUid );
    }

    @Override
    @JsonProperty
    @JacksonXmlElementWrapper( localName = "translations", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "translation", namespace = DxfNamespaces.DXF_2_0 )
    public Set<Translation> getTranslations()
    {
        return translations != null ? translations : new HashSet<>();
    }

    /**
     * Clears out cache when setting translations.
     */
    public void setTranslations( Set<Translation> translations )
    {
        this.translationCache.clear();
        this.translations = translations;
    }

    /**
     * Returns a translated value for this object for the given property. The
     * current locale is read from the user context.
     *
     * @param property     the translation property.
     * @param defaultValue the value to use if there are no translations.
     * @return a translated value.
     */
    protected String getTranslation( TranslationProperty property, String defaultValue )
    {
        Locale locale = UserContext.getUserSetting( UserSettingKey.DB_LOCALE, Locale.class );

        defaultValue = defaultValue != null ? defaultValue.trim() : null;

        if ( locale == null || property == null )
        {
            return defaultValue;
        }

        loadTranslationsCacheIfEmpty();

        String cacheKey = Translation.getCacheKey( locale.toString(), property );

        return translationCache.getOrDefault( cacheKey, defaultValue );
    }

    /**
     * Populates the translationsCache map unless it is already populated.
     */
    private void loadTranslationsCacheIfEmpty()
    {
        if ( translationCache.isEmpty() && translations != null )
        {
            for ( Translation translation : translations )
            {
                if ( translation.getLocale() != null && translation.getProperty() != null && !StringUtils
                    .isEmpty( translation.getValue() ) )
                {
                    String key = Translation.getCacheKey( translation.getLocale(), translation.getProperty() );
                    translationCache.put( key, translation.getValue() );
                }
            }
        }
    }

    private void loadAttributeValuesCacheIfEmpty()
    {
        if ( cacheAttributeValues.isEmpty() && attributeValues != null )
        {
            attributeValues.forEach( av -> cacheAttributeValues.put( av.getAttribute().getUid(), av ) );
        }
    }

    @Override
    @JsonProperty
    @JsonSerialize( using = UserPropertyTransformer.JacksonSerialize.class )
    @JsonDeserialize( using = UserPropertyTransformer.JacksonDeserialize.class )
    @PropertyTransformer( UserPropertyTransformer.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    @PropertyRange( min = 8, max = 8 )
    public String getPublicAccess()
    {
        return publicAccess;
    }

    public void setPublicAccess( String publicAccess )
    {
        this.publicAccess = publicAccess;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean getExternalAccess()
    {
        return externalAccess;
    }

    public void setExternalAccess( Boolean externalAccess )
    {
        this.externalAccess = externalAccess == null ? false : externalAccess;
    }

    @Override
    @JsonProperty
    @JacksonXmlElementWrapper( localName = "userGroupAccesses", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "userGroupAccess", namespace = DxfNamespaces.DXF_2_0 )
    public Set<UserGroupAccess> getUserGroupAccesses()
    {
        return userGroupAccesses;
    }

    public void setUserGroupAccesses( Set<UserGroupAccess> userGroupAccesses )
    {
        this.userGroupAccesses = userGroupAccesses;
    }

    @Override
    @JsonProperty
    @JacksonXmlElementWrapper( localName = "userAccesses", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "userAccess", namespace = DxfNamespaces.DXF_2_0 )
    public Set<UserAccess> getUserAccesses()
    {
        return userAccesses;
    }

    public void setUserAccesses( Set<UserAccess> userAccesses )
    {
        this.userAccesses = userAccesses;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( localName = "access", namespace = DxfNamespaces.DXF_2_0 )
    public Access getAccess()
    {
        return access;
    }

    public void setAccess( Access access )
    {
        this.access = access;
    }

    @Override
    @JsonProperty
    @JacksonXmlElementWrapper( localName = "favorites", namespace = DxfNamespaces.DXF_2_0 )
    @JacksonXmlProperty( localName = "favorite", namespace = DxfNamespaces.DXF_2_0 )
    public Set<String> getFavorites()
    {
        return favorites;
    }

    public void setFavorites( Set<String> favorites )
    {
        this.favorites = favorites;
    }

    @Override
    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean isFavorite()
    {
        User user = UserContext.getUser();

        return user != null && favorites != null ? favorites.contains( user.getUid() ) : false;
    }

    @Override
    public boolean setAsFavorite( User user )
    {
        if ( this.favorites == null )
        {
            this.favorites = new HashSet<>();
        }

        return this.favorites.add( user.getUid() );
    }

    @Override
    public boolean removeAsFavorite( User user )
    {
        if ( this.favorites == null )
        {
            this.favorites = new HashSet<>();
        }

        return this.favorites.remove( user.getUid() );
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        int result = getUid() != null ? getUid().hashCode() : 0;
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);

        return result;
    }

    /**
     * Class check uses isAssignableFrom and get-methods to handle proxied objects.
     */
    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !getClass().isAssignableFrom( o.getClass() ) )
        {
            return false;
        }

        final BaseIdentifiableObject other = (BaseIdentifiableObject) o;

        if ( getUid() != null ? !getUid().equals( other.getUid() ) : other.getUid() != null )
        {
            return false;
        }

        if ( getCode() != null ? !getCode().equals( other.getCode() ) : other.getCode() != null )
        {
            return false;
        }

        if ( getName() != null ? !getName().equals( other.getName() ) : other.getName() != null )
        {
            return false;
        }

        return true;
    }

    /**
     * Equality check against typed identifiable object. This method is not
     * vulnerable to proxy issues, where an uninitialized object class type
     * fails comparison to a real class.
     *
     * @param other the identifiable object to compare this object against.
     * @return true if equal.
     */
    public boolean typedEquals( IdentifiableObject other )
    {
        if ( other == null )
        {
            return false;
        }

        if ( getUid() != null ? !getUid().equals( other.getUid() ) : other.getUid() != null )
        {
            return false;
        }

        if ( getCode() != null ? !getCode().equals( other.getCode() ) : other.getCode() != null )
        {
            return false;
        }

        if ( getName() != null ? !getName().equals( other.getName() ) : other.getName() != null )
        {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Set auto-generated fields on save or update
     */
    public void setAutoFields()
    {
        if ( uid == null || uid.length() == 0 )
        {
            setUid( CodeGenerator.generateUid() );
        }

        Date date = new Date();

        if ( created == null )
        {
            created = date;
        }

        setLastUpdated( date );
    }

    /**
     * Returns the value of the property referred to by the given IdScheme.
     *
     * @param idScheme the IdScheme.
     * @return the value of the property referred to by the IdScheme.
     */
    @Override
    public String getPropertyValue( IdScheme idScheme )
    {
        if ( idScheme.isNull() || idScheme.is( IdentifiableProperty.UID ) )
        {
            return uid;
        }
        else if ( idScheme.is( IdentifiableProperty.CODE ) )
        {
            return code;
        }
        else if ( idScheme.is( IdentifiableProperty.NAME ) )
        {
            return name;
        }
        else if ( idScheme.is( IdentifiableProperty.ID ) )
        {
            return id > 0 ? String.valueOf( id ) : null;
        }
        else if ( idScheme.is( IdentifiableProperty.ATTRIBUTE ) )
        {
            for ( AttributeValue attributeValue : attributeValues )
            {
                if ( idScheme.getAttribute().equals( attributeValue.getAttribute().getUid() ) )
                {
                    return attributeValue.getValue();
                }
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + getId() + "\", " +
            "\"uid\":\"" + getUid() + "\", " +
            "\"code\":\"" + getCode() + "\", " +
            "\"name\":\"" + getName() + "\", " +
            "\"created\":\"" + getCreated() + "\", " +
            "\"lastUpdated\":\"" + getLastUpdated() + "\" " +
            "}";
    }
}
