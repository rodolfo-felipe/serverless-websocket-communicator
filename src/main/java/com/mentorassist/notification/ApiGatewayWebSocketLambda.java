package com.mentorassist.notification;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import io.quarkus.amazon.lambda.runtime.JacksonInputReader;
import io.quarkus.amazon.lambda.runtime.JacksonOutputWriter;
import io.quarkus.amazon.lambda.runtime.LambdaInputReader;
import io.quarkus.amazon.lambda.runtime.LambdaOutputWriter;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.quarkus.amazon.lambda.runtime.AmazonLambdaMapperRecorder.objectMapper;

@Named("api-gateway-websocket")
public class ApiGatewayWebSocketLambda implements RequestStreamHandler {

  private static final Logger log = Logger.getLogger(ApiGatewayWebSocketLambda.class.getName());

  @Inject
  EventContextFactory eventContextFactory;

  Map<Class<?>, LambdaInputReader> readers = new HashMap<>();
  Map<Class<?>, LambdaOutputWriter> writers = new HashMap<>();

  @Override
  public void handleRequest(
      InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

    byte[] input = inputStream.readAllBytes();

    if (log.isLoggable(Level.FINE)) {
      log.fine(String.format(
          "Handling request: %s", objectMapper.readTree(input).toString()));
    }

    EventContext eventContext = eventContextFactory.create(input);

    var objectReader = getObjectReader(eventContext.getInputClass());
    var objectWriter = getObjectWriter(eventContext.getOutputClass());
    var request = objectReader.readValue(new ByteArrayInputStream(input));

    var response = eventContext.getHandler().handleRequest(request, context);
    objectWriter.writeValue(outputStream, response);
  }

  private LambdaInputReader getObjectReader(Class inputClass) {
    if (!readers.containsKey(inputClass)) {
      readers.put(inputClass, new JacksonInputReader(objectMapper.readerFor(inputClass)));
    }
    return readers.get(inputClass);
  }

  private LambdaOutputWriter getObjectWriter(Class outputClass) {
    if (!writers.containsKey(outputClass)) {
      writers.put(outputClass, new JacksonOutputWriter(objectMapper.writerFor(outputClass)));
    }
    return writers.get(outputClass);
  }
}
