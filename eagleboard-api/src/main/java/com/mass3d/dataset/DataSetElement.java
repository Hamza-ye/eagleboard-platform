package com.mass3d.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mass3d.category.CategoryCombo;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
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
  private CategoryCombo categoryCombo;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------
  public DataSetElement() {

  }

  public DataSetElement(DataSet dataSet, DataElement dataElement) {
    this.dataSet = dataSet;
    this.dataElement = dataElement;
  }

  public DataSetElement( DataSet dataSet, DataElement dataElement, CategoryCombo categoryCombo )
  {
    this.dataSet = dataSet;
    this.dataElement = dataElement;
    this.categoryCombo = categoryCombo;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  /**
   * Returns the category combination of this data set element, if null,
   * then returns the category combination of the data element of this data
   * set element.
   */
  public CategoryCombo getResolvedCategoryCombo()
  {
    return hasCategoryCombo() ? getCategoryCombo() : dataElement.getCategoryCombo();
  }

  public boolean hasCategoryCombo()
  {
    return categoryCombo != null;
  }

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
        "\"categoryCombo\":\"" + categoryCombo + "\" " +
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

  /**
   * Category combination of this data set element. Can be null, use
   * {@link #getResolvedCategoryCombo} to get fall back to category
   * combination of data element.
   */
  @JsonProperty
  @JsonSerialize( as = BaseIdentifiableObject.class )
  @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
  public CategoryCombo getCategoryCombo()
  {
    return categoryCombo;
  }

  public void setCategoryCombo( CategoryCombo categoryCombo )
  {
    this.categoryCombo = categoryCombo;
  }

}
