package com.mass3d.outboundmessage;

import java.util.ArrayList;
import java.util.List;
import com.mass3d.common.DeliveryChannel;

public class OutboundMessageBatch {

  private final DeliveryChannel deliveryChannel;
  private List<OutboundMessage> messages = new ArrayList<>();

  public OutboundMessageBatch(List<OutboundMessage> messages, DeliveryChannel deliveryChannel) {
    this.messages = messages;
    this.deliveryChannel = deliveryChannel;
  }

  public OutboundMessageBatch(DeliveryChannel deliveryChannel) {
    this.deliveryChannel = deliveryChannel;
  }

  public List<OutboundMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<OutboundMessage> messages) {
    this.messages = messages;
  }

  public DeliveryChannel getDeliveryChannel() {
    return deliveryChannel;
  }

  public int size() {
    return messages.size();
  }
}
