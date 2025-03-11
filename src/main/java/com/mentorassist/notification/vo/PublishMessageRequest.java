package com.mentorassist.notification.vo;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PublishMessageRequest {

  private String caller;
  private String context;
  private String contextId;

  private String message;

  public String getCaller() {
    return caller;
  }

  public void setCaller(String caller) {
    this.caller = caller;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getContextId() {
    return contextId;
  }

  public void setContextId(String contextId) {
    this.contextId = contextId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "PublishMessageRequest{" +
        ", caller='" + caller + '\'' +
        ", context='" + context + '\'' +
        ", contextId='" + contextId + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
