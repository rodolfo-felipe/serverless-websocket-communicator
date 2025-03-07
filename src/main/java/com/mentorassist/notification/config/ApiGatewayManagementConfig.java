package com.mentorassist.notification.config;

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

  @Produces
  @ApplicationScoped
  public ApiGatewayManagementApiClient apiGatewayClient() {
    return ApiGatewayManagementApiClient.builder()
        .region(Region.of(region))
        .httpClient(UrlConnectionHttpClient.builder().build())
        .build();
  }
}
