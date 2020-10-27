package com.mass3d.todotask;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Sets;
import com.mass3d.activity.Activity;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.BaseNameableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.InterpretableObject;
import com.mass3d.common.MetadataObject;
import com.mass3d.dataset.DataSet;
import com.mass3d.interpretation.Interpretation;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "todotask")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "todotaskid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="todotaskusergroupaccesses",
//        joinColumns=@JoinColumn(name="todotaskid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="todotaskuseraccesses",
//        joinColumns=@JoinColumn(name="todotaskid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "todoTask", namespace = DxfNamespaces.DXF_2_0)
public class TodoTask
    extends BaseNameableObject
    implements MetadataObject, InterpretableObject {

  @ManyToMany (mappedBy="sources")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  Set<DataSet> dataSets = new HashSet<>();

  @ManyToOne
  @JoinTable(
      name = "activitytodotasks",
      joinColumns = @JoinColumn(name = "todotaskid", insertable = false,
          updatable = false, referencedColumnName = "todotaskid"),
      inverseJoinColumns = @JoinColumn(name = "activityid", insertable = false,
          updatable = false, referencedColumnName = "activityid")
  )
  private Activity activity;

  /**
   * Property indicating if the todoTask could be collected using mobile data entry.
   */
  private boolean mobile;

  /**
   * Interpretations of this todoTask.
   */
  @OneToMany(mappedBy = "todoTask")
  private Set<Interpretation> interpretations = new HashSet<>();
  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public TodoTask() {
  }

  public TodoTask(String name) {
    this();
    this.name = name;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public boolean addFieldSet(DataSet dataSet) {
    dataSets.add(dataSet);
    return dataSet.getSources().add(this);
  }

  public void updateFieldSets(Set<DataSet> updates) {
    Set<DataSet> toRemove = Sets.difference(dataSets, updates);
    Set<DataSet> toAdd = Sets.difference(updates, dataSets);

    toRemove.forEach(u -> u.getSources().remove(this));
    toAdd.forEach(u -> u.getSources().add(this));

    dataSets.clear();
    dataSets.addAll(updates);
  }

  // ----------------------------------------------------------------------------
  // Getters and Setters
  // ----------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "dataSets", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "fieldSet", namespace = DxfNamespaces.DXF_2_0)
  public Set<DataSet> getDataSets() {
    return dataSets;
  }

  public void setDataSets(Set<DataSet> dataSets) {
    this.dataSets = dataSets;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public boolean isMobile() {
    return mobile;
  }

  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }

  @Override
  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "interpretations", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "interpretation", namespace = DxfNamespaces.DXF_2_0)
  public Set<Interpretation> getInterpretations() {
    return interpretations;
  }

  public void setInterpretations(Set<Interpretation> interpretations) {
    this.interpretations = interpretations;
  }
}
