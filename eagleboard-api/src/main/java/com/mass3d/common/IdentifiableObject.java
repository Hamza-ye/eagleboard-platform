package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import com.mass3d.security.acl.Access;
import com.mass3d.translation.Translation;
import com.mass3d.translation.TranslationProperty;
import com.mass3d.user.User;
import com.mass3d.user.UserAccess;
import com.mass3d.user.UserGroupAccess;

public interface IdentifiableObject
    extends LinkableObject, Comparable<IdentifiableObject>, Serializable
{
//    String[] I18N_PROPERTIES = { TranslationProperty.NAME.getName() };

    long getId();

    String getUid();

    String getCode();

    String getName();

    String getDisplayName();

    Date getCreated();

    Date getLastUpdated();

    User getLastUpdatedBy();

//    Set<AttributeValue> getAttributeValues();
//
//    void setAttributeValues(Set<AttributeValue> attributeValues);

//    Set<Translation> getTranslations();

    Set<String> getFavorites();

    boolean isFavorite();

    boolean setAsFavorite(User user);

    boolean removeAsFavorite(User user);

    //-----------------------------------------------------------------------------
    // Sharing
    //-----------------------------------------------------------------------------

    User getUser();

    String getPublicAccess();

    boolean getExternalAccess();

    Set<UserGroupAccess> getUserGroupAccesses();

    Set<UserAccess> getUserAccesses();

    Access getAccess();

    //-----------------------------------------------------------------------------
    // Utility methods
    //-----------------------------------------------------------------------------

    @JsonIgnore
    String getPropertyValue(IdScheme idScheme);
}
