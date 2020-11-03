package com.mass3d.datavalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;
import java.util.Date;
import java.util.Objects;
import com.mass3d.common.AuditType;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.dataelement.DataElement;
import com.mass3d.todotask.TodoTask;
import com.mass3d.period.Period;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Entity
//@Table(name = "datavalueaudit")
public class DataValueAudit {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE
  )
  private int id;

  @ManyToOne
  @JoinColumn(name = "datafieldid")
  private DataElement dataElement;

  @ManyToOne
  @JoinColumn(name = "periodid")
  private Period period;

  @ManyToOne
  @JoinColumn(name = "todotaskid")
  private TodoTask todoTask;

  private String value;

  private String modifiedBy;

  private Date created;

  private AuditType auditType;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DataValueAudit() {
  }

  public DataValueAudit(DataValue dataValue, String value, String modifiedBy, AuditType auditType) {
    this.dataElement = dataValue.getDataElement();
    this.period = dataValue.getPeriod();
    this.todoTask = dataValue.getSource();
    this.value = value;
    this.modifiedBy = modifiedBy;
    this.created = new Date();
    this.auditType = auditType;
  }

  public DataValueAudit(DataElement dataElement, Period period, TodoTask todoTask,
      String value, String modifiedBy, AuditType auditType) {
    this.dataElement = dataElement;
    this.period = period;
    this.todoTask = todoTask;
    this.value = value;
    this.modifiedBy = modifiedBy;
    this.created = new Date();
    this.auditType = auditType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataElement, period, todoTask, value, modifiedBy, created, auditType);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    final DataValueAudit other = (DataValueAudit) object;

    return Objects.equals(this.dataElement, other.dataElement)
        && Objects.equals(this.period, other.period)
        && Objects.equals(this.todoTask, other.todoTask)
        && Objects.equals(this.value, other.value)
        && Objects.equals(this.modifiedBy, other.modifiedBy)
        && Objects.equals(this.created, other.created)
        && Objects.equals(this.auditType, other.auditType);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("data field", dataElement)
        .add("period", period)
        .add("todo task", todoTask)
        .add("value", value)
        .add("modified by", modifiedBy)
        .add("created", created)
        .add("audit type", auditType).toString();
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataElement getDataElement() {
    return dataElement;
  }

  public void setDataElement(DataElement dataElement) {
    this.dataElement = dataElement;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public TodoTask getTodoTask() {
    return todoTask;
  }

  public void setTodoTask(TodoTask todoTask) {
    this.todoTask = todoTask;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public AuditType getAuditType() {
    return auditType;
  }

  public void setAuditType(AuditType auditType) {
    this.auditType = auditType;
  }
}
