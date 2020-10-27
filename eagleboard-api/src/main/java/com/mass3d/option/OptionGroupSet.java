package com.mass3d.option;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseDimensionalObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DimensionType;
import com.mass3d.common.DimensionalItemObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "optiongroupset")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "optiongroupsetid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="optiongroupsetusergroupaccesses",
//        joinColumns=@JoinColumn(name="optiongroupsetid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="optiongroupsetuseraccesses",
//        joinColumns=@JoinColumn(name="optiongroupsetid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "optionGroupSet", namespace = DxfNamespaces.DXF_2_0)
public class OptionGroupSet
    extends BaseDimensionalObject implements MetadataObject {

  @ManyToMany
  @JoinTable(name = "optiongroupsetmembers",
      joinColumns = @JoinColumn(name = "optiongroupsetid", referencedColumnName = "optiongroupsetid"),
      inverseJoinColumns = @JoinColumn(name = "optiongroupid", referencedColumnName = "optiongroupid")
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<OptionGroup> members = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "optionsetid")
  private OptionSet optionSet;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public OptionGroupSet() {
  }

  public OptionGroupSet(String name) {
    this.name = name;
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  @JsonProperty("optionGroups")
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "optionGroups", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "optionGroup", namespace = DxfNamespaces.DXF_2_0)
  public List<OptionGroup> getMembers() {
    return members;
  }

  public void setMembers(List<OptionGroup> members) {
    this.members = members;
  }

  @JsonProperty("optionSet")
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(localName = "optionSet", namespace = DxfNamespaces.DXF_2_0)
  public OptionSet getOptionSet() {
    return optionSet;
  }

  public void setOptionSet(OptionSet optionSet) {
    this.optionSet = optionSet;
  }

  // -------------------------------------------------------------------------
  // Dimensional object
  // -------------------------------------------------------------------------

  @Override
  @JsonProperty
  @JsonSerialize(contentAs = BaseDimensionalItemObject.class)
  @JacksonXmlElementWrapper(localName = "items", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "item", namespace = DxfNamespaces.DXF_2_0)
  public List<DimensionalItemObject> getItems() {
    return Lists.newArrayList(members);
  }

  @Override
  public DimensionType getDimensionType() {
    return DimensionType.OPTION_GROUP_SET;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public void addOptionGroup(OptionGroup optionGroup) {
    members.add(optionGroup);
  }

  public void removeOptionGroup(OptionGroup optionGroup) {
    members.remove(optionGroup);
  }

  public void removeAllOptionGroups() {
    members.clear();
  }

  public Collection<Option> getOptions() {
    List<Option> options = new ArrayList<>();

    for (OptionGroup group : members) {
      options.addAll(group.getMembers());
    }

    return options;
  }

  public Boolean isMemberOfOptionGroups(Option option) {
    for (OptionGroup group : members) {
      if (group.getMembers().contains(option)) {
        return true;
      }
    }

    return false;
  }

  public Boolean hasOptionGroups() {
    return members != null && members.size() > 0;
  }

  public List<OptionGroup> getSortedGroups() {
    List<OptionGroup> sortedGroups = new ArrayList<>(members);

    Collections.sort(sortedGroups);

    return sortedGroups;
  }
}
