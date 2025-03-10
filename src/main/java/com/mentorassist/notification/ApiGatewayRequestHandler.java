package com.mentorassist.notification;

import static com.mentorassist.notification.util.Constants.COOKIE_AUTH_INTEGRATION;
import static com.mentorassist.notification.util.Constants.HEADER_AUTHORIZATION;
import static com.mentorassist.notification.util.Constants.HEADER_COOKIE;
import static com.mentorassist.notification.util.Constants.PARAM_CONTEXT_ID;
import static com.mentorassist.notification.util.Constants.PARAM_CONTEXT_TYPE;
import static java.time.Instant.ofEpochMilli;
import static org.jboss.logmanager.Level.ERROR;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import com.mentorassist.notification.exception.MissingParameterException;
import com.mentorassist.notification.service.AuthenticationService;
import com.mentorassist.notification.service.WebSocketSessionService;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import software.amazon.awssdk.http.HttpStatusCode;

@ApplicationScoped
public class ApiGatewayRequestHandler
    implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse> {

  private static final Logger log = Logger.getLogger(ApiGatewayRequestHandler.class.getName());

  @Inject
  WebSocketSessionService sessionService;

  @Inject
  AuthenticationService authenticationService;

  @Override
  public APIGatewayV2WebSocketResponse handleRequest(
      APIGatewayV2WebSocketEvent input, Context context) {

    var routeKey = input.getRequestContext().getRouteKey();
    var connectionId = input.getRequestContext().getConnectionId();
    var connectedAt = ofEpochMilli(input.getRequestContext().getConnectedAt())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

    var response = new APIGatewayV2WebSocketResponse();
    response.setStatusCode(HttpStatusCode.OK);

    try {
      switch (routeKey) {
        case "$connect":
          var user = authenticationService.authenticate(getAccessToken(input));
          var contextType = getQueryParam(input, PARAM_CONTEXT_TYPE);
          var contextId = getQueryParam(input, PARAM_CONTEXT_ID);

          sessionService.connect(
              connectionId, user.getUcode(), contextType, contextId, connectedAt);
          break;
        case "$disconnect":
          sessionService.disconnect(connectionId);
          break;
        default:
          throw new IllegalArgumentException("Received an invalid route key!");
      }
    } catch (MissingParameterException ex) {
      log.log(ERROR, ex.getMessage(), ex);
      setResponseFailureData(response, HttpStatusCode.BAD_REQUEST, ex.getMessage());

    } catch (Exception ex) {
      log.log(ERROR, ex.getMessage(), ex);
      setResponseFailureData(response, HttpStatusCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    return response;
  }

  private String getAccessToken(APIGatewayV2WebSocketEvent input) {
    String token = getHeader(input, HEADER_AUTHORIZATION)
        .or(() -> getCookies(input).map(c -> c.get(COOKIE_AUTH_INTEGRATION)))
        .orElseThrow(() -> new MissingParameterException("token"));

    if (token.toLowerCase().startsWith("bearer ")) {
      token = token.substring(7);
    }
    return token;
  }


  private Optional<String> getHeader(APIGatewayV2WebSocketEvent input, String header) {
    return input.getHeaders().entrySet().stream()
        .filter(e -> header.equalsIgnoreCase(e.getKey()))
        .map(Map.Entry::getValue)
        .findFirst();
  }

  private Optional<Map<String, String>> getCookies(APIGatewayV2WebSocketEvent input) {
    return getHeader(input, HEADER_COOKIE).map(
        header -> Arrays.stream(header.split("; "))
            .map(entry -> entry.split("="))
            .collect(Collectors.toMap(i -> i[0], i -> i[1]))
    );
  }

  private String getQueryParam(APIGatewayV2WebSocketEvent input, String param) {
    return Optional.ofNullable(input.getQueryStringParameters())
        .map(it -> it.get(param))
        .orElseThrow(() -> new MissingParameterException(param));
  }

  private void setResponseFailureData(
      APIGatewayV2WebSocketResponse response, int status, String errorMessage) {
    response.setStatusCode(status);
    response.setBody(errorMessage);
  }
}