package com.mass3d.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.activity.Activity;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.IdentifiableObject;
import com.mass3d.dataset.DataSet;
import com.mass3d.period.Period;
import com.mass3d.period.PeriodType;
import com.mass3d.project.Project;
import com.mass3d.todotask.TodoTask;
import com.mass3d.user.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "interpretation")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "interpretationid"))
//@AssociationOverride(
//    name = "userGroupAccesses",
//    joinTable = @JoinTable(
//        name = "interpretationusergroupaccesses",
//        joinColumns = @JoinColumn(name = "interpretationid"),
//        inverseJoinColumns = @JoinColumn(name = "usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name = "userAccesses",
//    joinTable = @JoinTable(
//        name = "interpretationuseraccesses",
//        joinColumns = @JoinColumn(name = "interpretationid"),
//        inverseJoinColumns = @JoinColumn(name = "useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "interpretation", namespace = DxfNamespaces.DXF_2_0)
public class Interpretation
    extends BaseIdentifiableObject {

  @ManyToOne
  @JoinColumn(name = "projectid")
  private Project project;

  @ManyToOne
  @JoinColumn(name = "activityid")
  private Activity activity;

  @ManyToOne
  @JoinColumn(name = "fieldsetid")
  private DataSet dataSet;

  @ManyToOne
  @JoinColumn(name = "todotaskid")
  private TodoTask todoTask;

  private Period period; // Applicable to report table and data set report

  private String text;

  @ManyToMany
  @JoinTable(name = "interpretation_comments",
      joinColumns = @JoinColumn(name = "interpretationid", referencedColumnName = "interpretationid"),
      inverseJoinColumns = @JoinColumn(name = "interpretationcommentid", referencedColumnName = "interpretationcommentid")
  )
  private List<InterpretationComment> comments = new ArrayList<>();

  private int likes;

  @ManyToMany
  @JoinTable(name = "intepretation_likedby",
      joinColumns = @JoinColumn(name = "interpretationid", referencedColumnName = "interpretationid"),
      inverseJoinColumns = @JoinColumn(name = "userid", referencedColumnName = "userid")
  )
  private Set<User> likedBy = new HashSet<>();

  private List<Mention> mentions = new ArrayList<>();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public Interpretation() {
  }

  public Interpretation(DataSet dataSet, Period period, TodoTask todoTask, String text) {
    this.dataSet = dataSet;
    this.period = period;
    this.todoTask = todoTask;
    this.text = text;
  }

  public Interpretation(DataSet dataSet, TodoTask todoTask, String text) {
    this.dataSet = dataSet;
    this.todoTask = todoTask;
    this.text = text;
  }

  public Interpretation(Activity activity, TodoTask todoTask, String text) {
    this.activity = activity;
    this.todoTask = todoTask;
    this.text = text;
  }
  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  /**
   * Overriding getUser in order to expose user in web api. Sharing is not enabled for
   * interpretations but "user" is used for representing the creator. Must be removed when sharing
   * is enabled for this class.
   */
  @Override
  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public User getUser() {
    return user;
  }

  public IdentifiableObject getObject() {
    if (dataSet != null) {
      return dataSet;
    } else if (todoTask != null) {
      return todoTask;
    }
    return null;
  }

  public void addComment(InterpretationComment comment) {
    this.comments.add(comment);
  }

  public boolean isFieldSetReportInterpretation() {
    return dataSet != null;
  }

  public PeriodType getPeriodType() {
    return period != null ? period.getPeriodType() : null;
  }

  /**
   * Attempts to add the given user to the set of users liking this interpretation. If user not
   * already present, increments the like count with one.
   *
   * @param user the user liking this interpretation.
   * @return true if the given user had not already liked this interpretation.
   */
  public boolean like(User user) {
    boolean like = this.likedBy.add(user);

    if (like) {
      this.likes++;
    }

    return like;
  }

  /**
   * Attempts to remove the given user from the set of users liking this interpretation. If user not
   * already present, decrease the like count with one.
   *
   * @param user the user removing the like from this interpretation.
   * @return true if the given user had previously liked this interpretation.
   */
  public boolean unlike(User user) {
    boolean unlike = this.likedBy.remove(user);

    if (unlike) {
      this.likes--;
    }

    return unlike;
  }

  // -------------------------------------------------------------------------
  // Get and set methods
  // -------------------------------------------------------------------------

  @Override
  public String getName() {
    return uid;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataSet getDataSet() {
    return dataSet;
  }

  public void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public TodoTask getTodoTask() {
    return todoTask;
  }

  public void setTodoTask(TodoTask todoTask) {
    this.todoTask = todoTask;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "comments", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "comment", namespace = DxfNamespaces.DXF_2_0)
  public List<InterpretationComment> getComments() {
    return comments;
  }

  public void setComments(List<InterpretationComment> comments) {
    this.comments = comments;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  @JsonProperty("likedBy")
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "likedBy", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "likeByUser", namespace = DxfNamespaces.DXF_2_0)
  public Set<User> getLikedBy() {
    return likedBy;
  }

  public void setLikedBy(Set<User> likedBy) {
    this.likedBy = likedBy;
  }

  @JsonProperty("mentions")
  @JacksonXmlElementWrapper(localName = "mentions", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "mentions", namespace = DxfNamespaces.DXF_2_0)
  public List<Mention> getMentions() {
    return mentions;
  }

  public void setMentions(List<Mention> mentions) {
    this.mentions = mentions;
  }

  @JsonIgnore
  public void setMentionsFromUsers(Set<User> users) {
    this.mentions = MentionUtils.convertUsersToMentions(users);
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

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Activity getActivity() {
    return activity;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }
}
