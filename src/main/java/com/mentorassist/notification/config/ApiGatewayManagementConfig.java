package com.mentorassist.notification.config;

import java.net.URI;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigatewaymanagementapi.ApiGatewayManagementApiClient;

@ApplicationScoped
public class ApiGatewayManagementConfig {

  @ConfigProperty(name = "aws.gateway.region")
  String region;

  @ConfigProperty(name = "aws.gateway.callback-endpoint")
  String endpoint;

  @Produces
  @ApplicationScoped
  public ApiGatewayManagementApiClient apiGatewayClient() {
    return ApiGatewayManagementApiClient.builder()
        .region(Region.of(region))
        .endpointOverride(URI.create(endpoint))
        .httpClient(UrlConnectionHttpClient.builder().build())
        .build();
  }
}
