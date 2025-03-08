package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SnsRequestHandler implements RequestHandler<SNSEvent, Void> {

  private static final Logger log = Logger.getLogger(SnsRequestHandler.class.getName());

  @Override
  public Void handleRequest(SNSEvent snsEvent, Context context) {
    log.fine("OK - Evento SNS recebido!");
    // TODO WILL BE IMPLEMENTED
    return null;
  }
}
