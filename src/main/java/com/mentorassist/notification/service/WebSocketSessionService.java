package com.mentorassist.notification.service;

import static com.mentorassist.notification.util.Constants.DB_CALLER_ID;
import static com.mentorassist.notification.util.Constants.DB_CONNECTED_AT;
import static com.mentorassist.notification.util.Constants.DB_CONNECTION_ID;
import static com.mentorassist.notification.util.Constants.DB_CONTEXT;
import static com.mentorassist.notification.util.Constants.DB_EXPIRATION_DATE;
import static com.mentorassist.notification.util.DbCompositeAttributeBuilder.getAttributeValueN;
import static com.mentorassist.notification.util.DbCompositeAttributeBuilder.getAttributeValueS;
import static com.mentorassist.notification.util.DbCompositeAttributeBuilder.getCallerIdAttributeValue;
import static com.mentorassist.notification.util.DbCompositeAttributeBuilder.getCompositeAttributeValueS;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@ApplicationScoped
public class WebSocketSessionService {

  private static final Logger log = Logger.getLogger(WebSocketSessionService.class.getName());

  @ConfigProperty(name = "websocket.ttl")
  long ttl;

  @ConfigProperty(name = "dynamodb.table")
  String tableName;

  @Inject
  DynamoDbClient dynamoDB;

  public void connect(
      String connectionId,
      String ucode,
      String contextType,
      String contextId,
      LocalDateTime connectedAt) {

    log.log(Level.FINE,
        "Saving session [ConnectionId: {0}, Ucode: {1}, ContexType: {2}, ContextId: {3}] on DynamoDB!",
        new String[]{connectionId, ucode, contextType, contextId});

    var item = Map.of(
        DB_CALLER_ID, getCallerIdAttributeValue(ucode),
        DB_CONTEXT, getCompositeAttributeValueS(contextType, contextId),
        DB_CONNECTION_ID, getAttributeValueS(connectionId),
        DB_CONNECTED_AT, getAttributeValueS(connectedAt.format(ISO_LOCAL_DATE_TIME)),
        DB_EXPIRATION_DATE, getAttributeValueN(String.valueOf(Instant.now().getEpochSecond() + ttl))
    );

    var request = PutItemRequest.builder()
        .tableName(tableName)
        .item(item)
        .build();

    dynamoDB.putItem(request);
  }
}