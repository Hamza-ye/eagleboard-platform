package com.mass3d.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.common.adapter.JacksonPeriodDeserializer;
import com.mass3d.common.adapter.JacksonPeriodSerializer;
import com.mass3d.period.Period;

//@Entity
//@Table(name = "datainputperiod")
@JacksonXmlRootElement(localName = "dataInputPeriods", namespace = DxfNamespaces.DXF_2_0)
public class DataInputPeriod implements EmbeddedObject {

  /**
   * The database internal identifier for this Object.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO
  )
  @Column(name = "datainputperiodid")
  private int id;

  /**
   * Period data must belong to
   */
  @ManyToOne
  @JoinColumn(name = "periodid")
  private Period period;

  /**
   * Opening date of which data can be entered
   */
  private Date openingDate;

  /**
   * Closing date of which data can no longer be entered
   */
  private Date closingDate;

  public DataInputPeriod() {
  }

  /**
   * Returns true if the period equals the DataInputPeriod's period
   *
   * @param period to check against
   * @return true if the two periods are equal
   */
  public boolean isPeriodEqual(Period period) {
    return this.period.equals(period);
  }

  /**
   * Returns true if the given date is after the openingDate and before the closing date If opening
   * date is null, all dates before closing date is valid. If closing date is null, all dates after
   * opening date is valid. If both opening and closing dates are null, all dates are valid
   *
   * @param date to check
   * @return true if date is between openingDate and closingDate
   */
  public boolean isDateWithinOpenCloseDates(Date date) {
    return (openingDate == null || date.after(openingDate))
        && (closingDate == null || date.before(closingDate));
  }

  /**
   * Checks whether a combination of Period and Date is valid for this DataInputPeriod. Returns true
   * if period is equal to this period, and date is between opening and closing dates if set.
   *
   * @return true if both period and date conforms to this DataInputPeriod.
   */
  public boolean isPeriodAndDateValid(Period period, Date date) {
    return isDateWithinOpenCloseDates(date) && isPeriodEqual(period);
  }

  @JsonIgnore
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty
  @JsonSerialize(using = JacksonPeriodSerializer.class)
  @JsonDeserialize(using = JacksonPeriodDeserializer.class)
  @JacksonXmlProperty(localName = "period", namespace = DxfNamespaces.DXF_2_0)
  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  @JsonProperty
  @JacksonXmlProperty(localName = "openingDate", namespace = DxfNamespaces.DXF_2_0)
  public Date getOpeningDate() {
    return openingDate;
  }

  public void setOpeningDate(Date openingDate) {
    this.openingDate = openingDate;
  }

  @JsonProperty
  @JacksonXmlProperty(localName = "closingDate", namespace = DxfNamespaces.DXF_2_0)
  public Date getClosingDate() {
    return closingDate;
  }

  public void setClosingDate(Date closingDate) {
    this.closingDate = closingDate;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    DataInputPeriod that = (DataInputPeriod) object;

    return new EqualsBuilder()
        .appendSuper(super.equals(object))
        .append(period, that.period)
        .append(openingDate, that.openingDate)
        .append(closingDate, that.closingDate)
        .isEquals();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("period", period)
        .add("openingDate", openingDate)
        .add("closingDate", closingDate)
        .toString();
  }
}
