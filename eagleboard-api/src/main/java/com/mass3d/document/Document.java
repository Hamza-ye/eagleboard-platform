package com.mass3d.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.mass3d.common.BaseIdentifiableObject;
import com.mass3d.common.DxfNamespaces;
import com.mass3d.common.MetadataObject;
import com.mass3d.fileresource.FileResource;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//@Entity
//@Table(name = "document")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@AttributeOverride(name="id", column=@Column(name="documentid"))
//@AssociationOverride(
//    name="userGroupAccesses",
//    joinTable=@JoinTable(
//        name="documentusergroupaccesses",
//        joinColumns=@JoinColumn(name="documentid"),
//        inverseJoinColumns=@JoinColumn(name="usergroupaccessid")
//    )
//)
//@AssociationOverride(
//    name="userAccesses",
//    joinTable=@JoinTable(
//        name="documentuseraccesses",
//        joinColumns=@JoinColumn(name="documentid"),
//        inverseJoinColumns=@JoinColumn(name="useraccessid")
//    )
//)
@JacksonXmlRootElement(localName = "document", namespace = DxfNamespaces.DXF_2_0)
public class Document
    extends BaseIdentifiableObject implements MetadataObject {

  /**
   * Can be either a valid URL, or the path (filename) of a file. If the external property is true,
   * this should be an URL. If the external property is false, this should be the filename
   */
  private String url;

  /**
   * A reference to the file associated with the Document. If document represents an URL or a file
   * uploaded before this property was added, this will be null.
   */
  @ManyToOne
  @JoinColumn(name = "fileresource")
  private FileResource fileResource;

  /**
   * Determines if this document refers to a file (!external) or URL (external).
   */
  private boolean external;

  /**
   * The content type of the file referred to by the document, or null if document refers to an URL
   */
  private String contentType;

  /**
   * Flags whether the file should be displayed in-browser or downloaded. true should trigger a
   * download of the file when accessing the document data
   */
  private Boolean attachment = false;

  public Document() {
  }

  public Document(String name, String url, boolean external, String contentType) {
    this.name = name;
    this.url = url;
    this.external = external;
    this.contentType = contentType;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  // @Property( PropertyType.URL )
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public boolean isExternal() {
    return external;
  }

  public void setExternal(boolean external) {
    this.external = external;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Boolean getAttachment() {
    return attachment;
  }

  public void setAttachment(Boolean attachment) {
    this.attachment = attachment;
  }

  // Should not be exposed in the api
  public FileResource getFileResource() {
    return fileResource;
  }

  public void setFileResource(FileResource fileResource) {
    this.fileResource = fileResource;
  }
}
