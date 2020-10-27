package com.mass3d.outboundmessage;

import java.util.Set;

public class OutboundMessage {

  private String subject;

  private String text;

  private Set<String> recipients;

  public OutboundMessage(String subject, String text, Set<String> recipients) {
    this.subject = subject;
    this.text = text;
    this.recipients = recipients;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Set<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(Set<String> recipients) {
    this.recipients = recipients;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
}
