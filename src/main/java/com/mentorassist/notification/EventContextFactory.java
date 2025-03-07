package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

import static io.quarkus.amazon.lambda.runtime.AmazonLambdaMapperRecorder.objectMapper;

@ApplicationScoped
public class EventContextFactory {

  @Inject
  SnsRequestHandler snsHandler;

  @Inject
  ApiGatewayRequestHandler apiGatewayHandler;

  public EventContext create(byte[] inputBytes) {

    if (isSns(inputBytes)) {
      return new EventContext() {
        @Override
        public Class getInputClass() {
          return SNSEvent.class;
        }

        @Override
        public Class getOutputClass() {
          return Void.class;
        }

        @Override
        public RequestHandler getHandler() {
          return snsHandler;
        }
      };

    } else {
      return new EventContext() {
        @Override
        public Class getInputClass() {
          return APIGatewayV2WebSocketEvent.class;
        }

        @Override
        public Class getOutputClass() {
          return APIGatewayV2WebSocketResponse.class;
        }

        @Override
        public RequestHandler getHandler() {
          return apiGatewayHandler;
        }
      };
    }
  }

  private boolean isSns(byte[] inputBytes) {
    try {
      var jsonNode = objectMapper.readTree(inputBytes);
      return jsonNode.has("Records") || jsonNode.has("records");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}