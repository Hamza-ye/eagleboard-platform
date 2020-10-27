package com.mass3d.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mass3d.common.EmbeddedObject;
import com.mass3d.dataelement.DataElement;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataSetElement implements EmbeddedObject {

  /**
   * The database internal identifier for this Object.
   */
  @Id
  @GeneratedValue(
      strategy = GenerationType.AUTO
  )
  private int id;

  /**
   * Data element set, never null.
   */
  @ManyToOne
  @JoinColumn(name = "datasetid")
  private DataSet dataSet;

  /**
   * Data element, never null.
   */
  @ManyToOne
  @JoinColumn(name = "dataelementid")
  private DataElement dataElement;

  /**
   * Category combination, potentially null.
   */
//  private DataElementCategoryCombo categoryCombo;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------
  public DataSetElement() {

  }

  public DataSetElement(DataSet dataSet, DataElement dataElement) {
    this.dataSet = dataSet;
    this.dataElement = dataElement;
  }

//  public DataSetElement( DataSet dataSet, DataElement dataelement, DataElementCategoryCombo categoryCombo )
//  {
//    this.dataSet = dataSet;
//    this.dataelement = dataelement;
//    this.categoryCombo = categoryCombo;
//  }

// -------------------------------------------------------------------------
  // Hash code and equals
  // -------------------------------------------------------------------------

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), dataSet, dataElement);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (!getClass().isAssignableFrom(object.getClass())) {
      return false;
    }

    DataSetElement other = (DataSetElement) object;

    return objectEquals(other);
  }

  public boolean objectEquals(DataSetElement other) {
    return dataSet.equals(other.getDataSet()) && dataElement.equals(other.getDataElement());
  }

  @Override
  public String toString() {
    return "{" +
        "\"class\":\"" + getClass() + "\", " +
        "\"dataSet\":\"" + dataSet + "\", " +
        "\"dataelement\":\"" + dataElement + "\" " +
//        "\"categoryCombo\":\"" + categoryCombo + "\" " +
        "}";
  }

  // -------------------------------------------------------------------------
  // Get and set methods
  // -------------------------------------------------------------------------

  @JsonIgnore
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public DataSet getDataSet() {
    return dataSet;
  }

  public void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  public DataElement getDataElement() {
    return dataElement;
  }

  public void setDataElement(DataElement dataElement) {
    this.dataElement = dataElement;
  }

}
