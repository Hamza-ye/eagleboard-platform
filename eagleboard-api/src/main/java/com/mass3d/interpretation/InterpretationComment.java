package com.mass3d.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.user.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Entity
//@Table(name = "interpretationcomment")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name = "id", column = @Column(name = "interpretationcommentid"))
//@AssociationOverride(
//    name = "userGroupAccesses",
//    joinTable = @JoinTable(
//        name = "interpretationcommentusergroupaccesses",
//        joinColumns = @JoinColumn(name = "interpretationcommentid"),
//        inverseJoinColumns = @JoinColumn(name = "usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name = "userAccesses",
//    joinTable = @JoinTable(
//        name = "interpretationcommentuseraccesses",
//        joinColumns = @JoinColumn(name = "interpretationcommentid"),
//        inverseJoinColumns = @JoinColumn(name = "useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "interpretationComment", namespace = DxfNamespaces.DXF_2_0)
public class InterpretationComment
    extends BaseIdentifiableObject {

  private String text;

//  private List<Mention> mentions = new ArrayList<>();

  public InterpretationComment() {
    this.created = new Date();
  }

  public InterpretationComment(String text) {
    this.text = text;
    this.created = new Date();
  }

  public InterpretationComment(String text, User user) {
    this.text = text;
    this.user = user;
    this.created = new Date();
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

//  @JsonProperty("mentions")
//  @JacksonXmlElementWrapper(localName = "mentions", namespace = DxfNamespaces.DXF_2_0)
//  @JacksonXmlProperty(localName = "mentions", namespace = DxfNamespaces.DXF_2_0)
//  public List<Mention> getMentions() {
//    return mentions;
//  }
//
//  public void setMentions(List<Mention> mentions) {
//    this.mentions = mentions;
//  }
//
//  @JsonIgnore
//  public void setMentionsFromUsers(Set<User> users) {
//    this.mentions = MentionUtils.convertUsersToMentions(users);
//  }
}
