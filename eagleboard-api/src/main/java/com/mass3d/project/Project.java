package com.mass3d.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.activity.Activity;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.BaseNameableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.InterpretableObject;
import com.mass3d.common.MetadataObject;
import com.mass3d.interpretation.Interpretation;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


//@Entity
//@Table(name = "project")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "projectid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="projectusergroupaccesses",
//        joinColumns=@JoinColumn(name="projectid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="projectuseraccesses",
//        joinColumns=@JoinColumn(name="projectid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "project", namespace = DxfNamespaces.DXF_2_0)
public class Project
    extends BaseNameableObject
    implements MetadataObject , InterpretableObject {

//  @OneToMany
//  @JoinTable(
//      name = "projectactivities",
//      joinColumns = @JoinColumn(name = "projectid", referencedColumnName = "projectid"),
//      inverseJoinColumns = @JoinColumn(name = "activityid", referencedColumnName = "activityid")
//  )
//  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<Activity> activities = new HashSet<>();

  /**
   * Interpretations of this project.
   */
//  @OneToMany(mappedBy = "project")
  private Set<Interpretation> interpretations = new HashSet<>();
  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------
  public Project() {
  }

  public Project(String name) {
    this();
    this.name = name;
  }

  public void addActivity(Activity activity) {
    activity.setProject(this);
    this.activities.add(activity);
  }

  public void removeActivity(Activity activity) {
    this.activities.remove(activity);
    activity.setProject(null);
  }
  // ----------------------------------------------------------------------------
  // Getters and Setters
  // ----------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "activities", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "activity", namespace = DxfNamespaces.DXF_2_0)
  public Set<Activity> getActivities() {
    return activities;
  }


  public void setActivities(Set<Activity> activities) {
    this.activities = activities;
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
