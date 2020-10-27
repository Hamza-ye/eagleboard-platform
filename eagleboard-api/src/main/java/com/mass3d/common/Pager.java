package com.mass3d.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "pager", namespace = DxfNamespaces.DXF_2_0)
public class Pager {

  public static final int DEFAULT_PAGE_SIZE = 50;

  private int page = 1;

  private long total = 0;

  private int pageSize = Pager.DEFAULT_PAGE_SIZE;

  private String nextPage;

  private String prevPage;

  public Pager() {

  }

  public Pager(int page, long total) {
    this.page = page;
    this.total = total;

    if (this.page > getPageCount()) {
      this.page = getPageCount();
    }

    if (this.page < 1) {
      this.page = 1;
    }
  }

  public Pager(int page, long total, int pageSize) {
    this.page = page;
    this.total = total >= 0 ? total : 0;
    this.pageSize = pageSize > 0 ? pageSize : 1;

    if (this.page > getPageCount()) {
      this.page = getPageCount();
    }

    if (this.page < 1) {
      this.page = 1;
    }
  }

  public String toString() {
    return "[Page: " + page + " total: " + total + " size: " + pageSize + " offset: " + getOffset()
        + "]";
  }

  public boolean pageSizeIsDefault() {
    return pageSize == Pager.DEFAULT_PAGE_SIZE;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getPage() {
    return page;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public long getTotal() {
    return total;
  }

  /**
   * How many items per page.
   *
   * @return Number of items per page.
   */
  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getPageSize() {
    return pageSize;
  }

  /**
   * How many pages in total
   *
   * @return Total number of pages
   */
  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getPageCount() {
    int pageCount = 1;
    long totalTmp = total;

    while (totalTmp > pageSize) {
      totalTmp -= pageSize;
      pageCount++;
    }

    return pageCount;
  }

  /**
   * Return the current offset to start at.
   *
   * @return Offset to start at
   */
  public int getOffset() {
    return (page * pageSize) - pageSize;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getNextPage() {
    return nextPage;
  }

  public void setNextPage(String nextPage) {
    this.nextPage = nextPage;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getPrevPage() {
    return prevPage;
  }

  public void setPrevPage(String prevPage) {
    this.prevPage = prevPage;
  }
}
