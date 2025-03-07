package com.mentorassist.notification.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@ApplicationScoped
public class AwsConfig {

  @Produces
  public AwsCredentialsProvider credentialsProvider() {
    return DefaultCredentialsProvider.builder().build();
  }
}
