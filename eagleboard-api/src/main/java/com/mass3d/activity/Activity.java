package com.mass3d.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseDimensionalItemObject;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.BaseNameableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.InterpretableObject;
import com.mass3d.common.MetadataObject;
import com.mass3d.interpretation.Interpretation;
import com.mass3d.project.Project;
import com.mass3d.todotask.TodoTask;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "activity")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name="id", column=@Column(name="activityid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="activityusergroupaccesses",
//        joinColumns=@JoinColumn(name="activityid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="activityuseraccesses",
//        joinColumns=@JoinColumn(name="activityid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "activity", namespace = DxfNamespaces.DXF_2_0)
public class Activity
    extends BaseDimensionalItemObject
    implements MetadataObject, InterpretableObject {

  @ManyToOne
  @JoinTable(name = "projectactivities",
      joinColumns = @JoinColumn(name = "activityid", insertable = false,
          updatable = false, referencedColumnName = "activityid"),
      inverseJoinColumns = @JoinColumn(name = "projectid", insertable = false,
          updatable = false, referencedColumnName = "projectid")
  )
  private Project project;

  @OneToMany
  @JoinTable(name = "activitytodotasks",
      joinColumns = @JoinColumn(name = "activityid", referencedColumnName = "activityid"),
      inverseJoinColumns = @JoinColumn(name = "todotaskid", referencedColumnName = "todotaskid")
  )
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<TodoTask> todoTasks = new HashSet<>();

  /**
   * Interpretations of this activity.
   */
  @OneToMany(mappedBy = "activity")
  private Set<Interpretation> interpretations = new HashSet<>();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public Activity() {
  }

  public Activity(String name) {
    this();
    this.name = name;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public void addTask(TodoTask task) {
    this.todoTasks.add(task);
    task.setActivity(this);
  }

  public void removeTask(TodoTask task) {
    this.todoTasks.remove(task);
    task.setActivity(null);
  }
  // ----------------------------------------------------------------------------
  // Getters and Setters
  // ----------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "todoTasks", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "todoTask", namespace = DxfNamespaces.DXF_2_0)
  public Set<TodoTask> getTodoTasks() {
    return todoTasks;
  }

  public void setTodoTasks(Set<TodoTask> todoTasks) {
    this.todoTasks = todoTasks;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
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
