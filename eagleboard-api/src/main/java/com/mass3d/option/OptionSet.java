package com.mass3d.option;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.IdScheme;
import com.mass3d.common.MetadataObject;
import com.mass3d.common.ValueType;
import com.mass3d.common.VersionedObject;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "optionset")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "optionsetid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="optionsetusergroupaccesses",
//        joinColumns=@JoinColumn(name="optionsetid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="optionsetuseraccesses",
//        joinColumns=@JoinColumn(name="optionsetid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "optionSet", namespace = DxfNamespaces.DXF_2_0)
public class OptionSet
    extends BaseIdentifiableObject
    implements VersionedObject, MetadataObject {

  @OneToMany(
      mappedBy = "optionSet"
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Option> options = new ArrayList<>();

  private ValueType valueType;

  private int version;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public OptionSet() {
  }

  public OptionSet(String name, ValueType valueType) {
    this.name = name;
    this.valueType = valueType;
  }

  public OptionSet(String name, ValueType valueType, List<Option> options) {
    this.name = name;
    this.valueType = valueType;
    this.options = options;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public void addOption(Option option) {
    if (option.getSortOrder() == null) {
      this.options.add(option);
    } else {
      boolean added = false;
      final int size = this.options.size();
      for (int i = 0; i < size; i++) {
        Option thisOption = this.options.get(i);
        if (thisOption.getSortOrder() == null || thisOption.getSortOrder() > option
            .getSortOrder()) {
          this.options.add(i, option);
          added = true;
          break;
        }
      }
      if (!added) {
        this.options.add(option);
      }
    }
    option.setOptionSet(this);
  }

  public void removeAllOptions() {
    options.clear();
  }

  @Override
  public int increaseVersion() {
    return ++version;
  }

  public List<String> getOptionValues() {
    return options.stream().map(Option::getName).collect(Collectors.toList());
  }

  public List<String> getOptionCodes() {
    return options.stream().map(Option::getCode).collect(Collectors.toList());
  }

  public Set<String> getOptionCodesAsSet() {
    return options.stream().map(Option::getCode).collect(Collectors.toSet());
  }

  public Option getOptionByCode(String code) {
    for (Option option : options) {
      if (option != null && option.getCode().equals(code)) {
        return option;
      }
    }

    return null;
  }

  public Map<String, String> getOptionCodePropertyMap(IdScheme idScheme) {
    return options.stream()
        .collect(Collectors.toMap(Option::getCode, o -> o.getPropertyValue(idScheme)));
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "options", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "option", namespace = DxfNamespaces.DXF_2_0)
  public List<Option> getOptions() {
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  @Override
  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }
}
