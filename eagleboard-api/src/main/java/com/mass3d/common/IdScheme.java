package com.mass3d.common;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import org.springframework.util.StringUtils;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
public class IdScheme {

  public static final IdScheme NULL = new IdScheme(null);
  public static final IdScheme ID = new IdScheme(IdentifiableProperty.ID);
  public static final IdScheme UID = new IdScheme(IdentifiableProperty.UID);
  public static final IdScheme UUID = new IdScheme(IdentifiableProperty.UUID);
  public static final IdScheme CODE = new IdScheme(IdentifiableProperty.CODE);
  public static final IdScheme NAME = new IdScheme(IdentifiableProperty.NAME);

  public static final ImmutableMap<IdentifiableProperty, IdScheme> IDPROPERTY_IDSCHEME_MAP =
      ImmutableMap.<IdentifiableProperty, IdScheme>builder().
          put(IdentifiableProperty.ID, IdScheme.ID).
          put(IdentifiableProperty.UID, IdScheme.UID).
          put(IdentifiableProperty.UUID, IdScheme.UUID).
          put(IdentifiableProperty.CODE, IdScheme.CODE).
          put(IdentifiableProperty.NAME, IdScheme.NAME).build();

  public static final String ATTR_ID_SCHEME_PREFIX = "ATTRIBUTE:";

  private IdentifiableProperty identifiableProperty;

  private String attribute;

  private IdScheme(IdentifiableProperty identifiableProperty) {
    this.identifiableProperty = identifiableProperty;
  }

  private IdScheme(IdentifiableProperty identifiableProperty, String attribute) {
    this.identifiableProperty = identifiableProperty;
    this.attribute = attribute;
  }

  public static IdScheme from(IdScheme idScheme) {
    if (idScheme == null) {
      return IdScheme.NULL;
    }

    return idScheme;
  }

  public static IdScheme from(String scheme) {
    if (scheme == null) {
      return IdScheme.NULL;
    }

    if (IdScheme.isAttribute(scheme)) {
      return new IdScheme(IdentifiableProperty.ATTRIBUTE, scheme.substring(10));
    }

    return IdScheme.from(IdentifiableProperty.valueOf(scheme.toUpperCase()));
  }

  public static IdScheme from(IdentifiableProperty property) {
    if (property == null) {
      return IdScheme.NULL;
    }

    return IDPROPERTY_IDSCHEME_MAP.containsKey(property) ?
        IDPROPERTY_IDSCHEME_MAP.get(property) : new IdScheme(property);
  }

  public static boolean isAttribute(String str) {
    return !StringUtils.isEmpty(str) && str.toUpperCase().startsWith(ATTR_ID_SCHEME_PREFIX)
        && str.length() == 21;
  }

  public IdentifiableProperty getIdentifiableProperty() {
    return identifiableProperty;
  }

  public void setIdentifiableProperty(IdentifiableProperty identifiableProperty) {
    this.identifiableProperty = identifiableProperty;
  }

  public String getIdentifiableString() {
    return identifiableProperty != null ? identifiableProperty.toString() : null;
  }

  public String getAttribute() {
    return attribute;
  }

  public boolean is(IdentifiableProperty identifiableProperty) {
    return this.identifiableProperty == identifiableProperty;
  }

  public boolean isNull() {
    return null == this.identifiableProperty;
  }

  public boolean isNotNull() {
    return !isNull();
  }

  public boolean isAttribute() {
    return IdentifiableProperty.ATTRIBUTE == identifiableProperty && !StringUtils
        .isEmpty(attribute);
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  /**
   * Returns a canonical String name representation of this id scheme.
   *
   * @return a canonical String name representation of this id scheme.
   */
  public String name() {
    if (IdentifiableProperty.ATTRIBUTE == identifiableProperty && attribute != null) {
      return ATTR_ID_SCHEME_PREFIX + attribute;
    } else {
      return identifiableProperty.name();
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("identifiableProperty", identifiableProperty)
        .add("attribute", attribute)
        .toString();
  }
}
