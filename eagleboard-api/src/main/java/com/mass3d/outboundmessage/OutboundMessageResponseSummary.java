package com.mass3d.outboundmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.mass3d.common.DeliveryChannel;
import com.mass3d.common.DxfNamespaces;

@JacksonXmlRootElement(localName = "messageResponseSummary", namespace = DxfNamespaces.DXF_2_0)
public class OutboundMessageResponseSummary {

  private int total;

  private int failed;

  private int pending;

  private int sent;

  private OutboundMessageBatchStatus batchStatus;

  private String responseMessage;

  private String errorMessage;

  private DeliveryChannel channel;

  public OutboundMessageResponseSummary() {
  }

  public OutboundMessageResponseSummary(String errorMessage, DeliveryChannel channel,
      OutboundMessageBatchStatus status) {
    this.errorMessage = errorMessage;
    this.channel = channel;
    this.batchStatus = status;
  }

  @JsonProperty(value = "total")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  @JsonProperty(value = "failed")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getFailed() {
    return failed;
  }

  public void setFailed(int failed) {
    this.failed = failed;
  }

  @JsonProperty(value = "pending")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getPending() {
    return pending;
  }

  public void setPending(int pending) {
    this.pending = pending;
  }

  @JsonProperty(value = "sent")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public int getSent() {
    return sent;
  }

  public void setSent(int sent) {
    this.sent = sent;
  }

  @JsonProperty(value = "status")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public OutboundMessageBatchStatus getBatchStatus() {
    return batchStatus;
  }

  public void setBatchStatus(OutboundMessageBatchStatus batchStatus) {
    this.batchStatus = batchStatus;
  }

  @JsonProperty(value = "responseMessage")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getResponseMessage() {
    return responseMessage;
  }

  public void setResponseMessage(String responseMessage) {
    this.responseMessage = responseMessage;
  }

  @JsonProperty(value = "errorMessage")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @JsonProperty(value = "batchType")
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DeliveryChannel getChannel() {
    return channel;
  }

  public void setChannel(DeliveryChannel channel) {
    this.channel = channel;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("total", total)
        .add("failed", failed)
        .add("pending", pending)
        .add("sent", sent)
        .add("batchStatus", batchStatus)
        .add("responseMessage", responseMessage)
        .add("errorMessage", errorMessage)
        .add("channel", channel)
        .toString();
  }
}
