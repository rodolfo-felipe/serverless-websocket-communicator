package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiGatewayRequestHandler
    implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse> {

  @Override
  public APIGatewayV2WebSocketResponse handleRequest(
      APIGatewayV2WebSocketEvent input, Context context) {

    var response = new APIGatewayV2WebSocketResponse();
    response.setStatusCode(200);

    // TODO WILL BE IMPLEMENTED
    return response;
  }
}
