package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SnsRequestHandler implements RequestHandler<SNSEvent, Void> {

  @Override
  public Void handleRequest(SNSEvent snsEvent, Context context) {
    // TODO WILL BE IMPLEMENTED
    return null;
  }
}
