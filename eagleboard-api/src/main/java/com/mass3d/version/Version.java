package com.mass3d.version;

public class Version {

  private int id;

  private String key;

  private String value;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public Version() {
  }

  public Version(String key, String value) {
    this.key = key;
    this.value = value;
  }

  // -------------------------------------------------------------------------
  // hashCode and equals
  // -------------------------------------------------------------------------

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null) {
      return false;
    }

    if (!(o instanceof Version)) {
      return false;
    }

    final Version other = (Version) o;

    return key.equals(other.getKey());
  }

  @Override
  public String toString() {
    return "[" + key + "]";
  }

  // -------------------------------------------------------------------------
  // Getter & Setter
  // -------------------------------------------------------------------------

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
