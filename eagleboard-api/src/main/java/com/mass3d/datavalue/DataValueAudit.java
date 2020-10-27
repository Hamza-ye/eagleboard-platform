package com.mass3d.datavalue;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
