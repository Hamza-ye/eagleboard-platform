package com.mass3d.outboundmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import java.util.List;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "batchResponseStatus", namespace = DxfNamespaces.DXF_2_0)
public class BatchResponseStatus {

  private List<OutboundMessageResponseSummary> summaries;

  public BatchResponseStatus(List<OutboundMessageResponseSummary> summaries) {
    this.summaries = summaries;
  }

  public boolean isOk() {
    return summaries.stream()
        .noneMatch(s -> s.getBatchStatus() != OutboundMessageBatchStatus.COMPLETED);
  }

  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  @JsonProperty(value = "summaries")
  public List<OutboundMessageResponseSummary> getSummaries() {
    return summaries;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("summaries", summaries).toString();
  }
}
