package com.mentorassist.notification.service;

import com.mentorassist.notification.exception.InvalidConnectionException;
import com.mentorassist.notification.exception.NoConnectionException;
import com.mentorassist.notification.util.Constants;
import com.mentorassist.notification.util.DbCompositeAttributeBuilder;
import com.mentorassist.notification.vo.PublishMessageRequest;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.GoneException;
import software.amazon.awssdk.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

@ApplicationScoped
public class WebSocketPublishService {

  private static final Logger log = Logger.getLogger(WebSocketPublishService.class.getName());

  @Inject
  DynamoDbClient dynamoDB;

  @ConfigProperty(name = "dynamodb.table")
  String tableName;

  @Inject
  ApiGatewayManagementApiClient websocketGateway;

  public void publishMessage(PublishMessageRequest request) {
    var connectionId =
        getCallerConnection(request.getCaller(), request.getContext(), request.getContextId());

    if (connectionId.isPresent()) {
      try {
        log.fine(String.format("Connection Id found: %s", connectionId.get()));

        var post = PostToConnectionRequest.builder()
            .connectionId(connectionId.get())
            .data(SdkBytes.fromUtf8String(request.getMessage()))
            .build();

        websocketGateway.postToConnection(post);
      } catch (GoneException e) {
        throw new InvalidConnectionException(request.getCaller(), connectionId.get(), e);
      }

    } else {
      throw new NoConnectionException(request.getCaller());
    }
  }

  private Optional<String> getCallerConnection(String callerId, String context, String contextId) {
    var request = QueryRequest.builder()
        .tableName(tableName)
        .keyConditionExpression(
            Constants.DB_CALLER_ID + " = :caller_id AND " + Constants.DB_CONTEXT + " = :context ")
        .expressionAttributeValues(Map.of(
            ":caller_id", DbCompositeAttributeBuilder.getCallerIdAttributeValue(callerId),
            ":context",
            DbCompositeAttributeBuilder.getCompositeAttributeValueS(context, contextId)))
        .build();

    return dynamoDB.query(request).items().stream()
        .map(map -> map.get("connection_id").s())
        .findFirst();
  }
}