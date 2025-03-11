package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.MessageAttribute;
import com.mentorassist.notification.service.WebSocketPublishService;
import com.mentorassist.notification.vo.PublishMessageRequest;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SnsRequestHandler implements RequestHandler<SNSEvent, Void> {

  private static final Logger log = Logger.getLogger(SnsRequestHandler.class.getName());

  @Inject
  WebSocketPublishService webSocketPublishService;

  @Override
  public Void handleRequest(SNSEvent snsEvent, Context context) {
    log.fine(String.format("Processing new SNS event %s", snsEvent.toString()));

    snsEvent.getRecords().forEach(r -> {
      var attributes = r.getSNS().getMessageAttributes();
      var request = new PublishMessageRequest();

      request.setMessage(r.getSNS().getMessage());
      request.setCaller(getRequiredAttr(attributes, "caller"));
      request.setContext(getRequiredAttr(attributes, "context"));
      request.setContextId(getRequiredAttr(attributes, "contextId"));

      log.fine(String.format("Message request: %s", request));

      webSocketPublishService.publishMessage(request);
    });
    return null;
  }

  private static String getRequiredAttr(Map<String, MessageAttribute> attributes,
      String requiredAttr) {
    return Objects.requireNonNull(attributes.get(requiredAttr), requiredAttr + " is required")
        .getValue();
  }
}