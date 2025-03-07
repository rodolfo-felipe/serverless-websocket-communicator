package com.mentorassist.notification.service;

import com.mentorassist.notification.security.Authenticator;
import com.mentorassist.notification.vo.User;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthenticationService {

  @Inject
  Authenticator authenticator;

  public User authenticate(String token) {
    return authenticator.authenticate(token);
  }
}
