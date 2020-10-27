package com.mass3d.option;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.schema.PropertyType;
import com.mass3d.schema.annotation.Property;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "optionvalue")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "optionvalueid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="optionvalueusergroupaccesses",
//        joinColumns=@JoinColumn(name="optionvalueid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="optionvalueuseraccesses",
//        joinColumns=@JoinColumn(name="optionvalueid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "option", namespace = DxfNamespaces.DXF_2_0)
public class Option
    extends BaseIdentifiableObject implements MetadataObject {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "optionsetid")
  private OptionSet optionSet;

  private Integer sortOrder;

  private String description;

  private String formName;

//    private ObjectStyle style;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public Option() {
    setAutoFields();
  }

  public Option(String name, String code) {
    this();
    this.name = name;
    this.code = code;
  }

  public Option(String name, String code, Integer sortOrder) {
    this();
    this.name = name;
    this.code = code;
    this.sortOrder = sortOrder;
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  @Override
  @JsonProperty
  @JacksonXmlProperty(isAttribute = true)
  @Property(PropertyType.TEXT)
  public String getCode() {
    return super.getCode();
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public OptionSet getOptionSet() {
    return optionSet;
  }

  public void setOptionSet(OptionSet optionSet) {
    this.optionSet = optionSet;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

//    @JsonProperty
//    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
//    public ObjectStyle getStyle()
//    {
//        return style;
//    }
//
//    public void setStyle( ObjectStyle style )
//    {
//        this.style = style;
//    }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getFormName() {
    return formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
