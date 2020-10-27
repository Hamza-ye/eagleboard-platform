package com.mass3d.datavalue;

import com.mass3d.dataelement.DataElement;
import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;
import com.mass3d.todotask.TodoTask;
import com.mass3d.period.Period;

/**
 * @version $Id: DataValue.java 4638 2008-02-25 10:06:47Z larshelg $
 */
//@Entity
//@Table(name = "datavalue")
public class DataValue
    implements Serializable {

  public static final String TRUE = "true";
  public static final String FALSE = "false";
  /**
   * Determines if a de-serialized file is compatible with this class.
   */
  private static final long serialVersionUID = 6269303850789110610L;
  private static final Pattern ZERO_PATTERN = Pattern.compile("^0(\\.0*)?$");

  // -------------------------------------------------------------------------
  // Persistent properties
  // -------------------------------------------------------------------------
  @MapsId("datafieldid")
  @ManyToOne
  @JoinColumn(name = "datafieldid")
  private DataElement dataElement;

  @MapsId("periodid")
  @ManyToOne
  @JoinColumn(name = "periodid")
  private Period period;

  @MapsId("todotaskid")
  @ManyToOne
  @JoinColumn(name = "sourceid")
  private TodoTask source;

  private String value;

  private String storedBy;

  private Date created;

  private Date lastUpdated;

  private String comment;

  private Boolean followup;

  private boolean deleted;

  // -------------------------------------------------------------------------
  // Transient properties
  // -------------------------------------------------------------------------

  private transient boolean auditValueIsSet = false;

  private transient boolean valueIsSet = false;

  private transient String auditValue;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DataValue() {
    this.created = new Date();
    this.lastUpdated = new Date();
  }

  /**
   * @param dataElement the data element.
   * @param period the period.
   * @param source the organisation unit.
   */
  public DataValue(DataElement dataElement, Period period, TodoTask source) {
    this.dataElement = dataElement;
    this.period = period;
    this.source = source;
    this.created = new Date();
    this.lastUpdated = new Date();
  }

  /**
   * @param dataElement the data element.
   * @param period the period.
   * @param source the organisation unit.
   * @param value the value.
   */
  public DataValue(DataElement dataElement, Period period, TodoTask source, String value) {
    this.dataElement = dataElement;
    this.period = period;
    this.source = source;
    this.value = value;
    this.created = new Date();
    this.lastUpdated = new Date();
  }

  /**
   * @param dataElement the data element.
   * @param period the period.
   * @param source the organisation unit.
   * @param value the value.
   * @param storedBy the user that stored this data value.
   * @param lastUpdated the time of the last update to this data value.
   * @param comment the comment.
   */
  public DataValue(DataElement dataElement, Period period, TodoTask source, String value,
      String storedBy, Date lastUpdated, String comment) {
    this.dataElement = dataElement;
    this.period = period;
    this.source = source;
    this.value = value;
    this.storedBy = storedBy;
    this.created = new Date();
    this.lastUpdated = lastUpdated;
    this.comment = comment;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  /**
   * Indicates whether the value is a zero.
   */
  public boolean isZero() {
    return dataElement != null && dataElement.getValueType().isNumeric() && value != null
        && ZERO_PATTERN.matcher(value).find();
  }

  /**
   * Indicates whether the value is null.
   */
  public boolean isNullValue() {
    return StringUtils.trimToNull(value) == null && StringUtils.trimToNull(comment) == null;
  }

  public boolean isFollowup() {
    return followup != null && followup;
  }

  public void setFollowup(Boolean followup) {
    this.followup = followup;
  }

  public boolean hasComment() {
    return comment != null && !comment.isEmpty();
  }

  public void toggleFollowUp() {
    if (this.followup == null) {
      this.followup = true;
    } else {
      this.followup = !this.followup;
    }
  }

  // -------------------------------------------------------------------------
  // hashCode and equals
  // -------------------------------------------------------------------------

  public void mergeWith(DataValue other) {
    this.value = other.getValue();
    this.storedBy = other.getStoredBy();
    this.created = other.getCreated();
    this.lastUpdated = other.getLastUpdated();
    this.comment = other.getComment();
    this.followup = other.isFollowup();
    this.deleted = other.isDeleted();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null) {
      return false;
    }

    if (!getClass().isAssignableFrom(o.getClass())) {
      return false;
    }

    final DataValue other = (DataValue) o;

    return
        dataElement.equals(other.getDataElement()) &&
            period.equals(other.getPeriod()) &&
            source.equals(other.getSource());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = result * prime + dataElement.hashCode();
    result = result * prime + period.hashCode();
    result = result * prime + source.hashCode();

    return result;
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    return "[Data field: " + dataElement.getUid() +
        ", period: " + period.getUid() +
        ", source: " + source.getUid() +
        ", value: " + value +
        ", deleted: " + deleted + "]";
  }

  public DataElement getDataElement() {
    return dataElement;
  }

  public void setDataElement(DataElement dataElement) {
    this.dataElement = dataElement;
  }

  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  public TodoTask getSource() {
    return source;
  }

  public void setSource(TodoTask source) {
    this.source = source;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    if (!auditValueIsSet) {
      this.auditValue = valueIsSet ? this.value : value;
      auditValueIsSet = true;
    }

    valueIsSet = true;

    this.value = value;
  }

  public String getStoredBy() {
    return storedBy;
  }

  public void setStoredBy(String storedBy) {
    this.storedBy = storedBy;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public String getAuditValue() {
    return auditValue;
  }
}