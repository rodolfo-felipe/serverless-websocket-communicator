package com.mentorassist.notification.security;

import com.mentorassist.notification.vo.User;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SingleSignOnAuthenticator implements Authenticator {

  @Override
  public User authenticate(String token) {
    // MOCK USER TO PROTECT ENTERPRISE SECURITY CODE

    var user = new User();
    user.setId(1L);
    user.setName("Rodolfo Menezes");
    user.setEmail("rodolfo.menezes@mailinator.com");
    user.setLogin("rodolfo.menezes@mailinator.com");
    user.setCountry("Brasil");
    user.setLocale("pt_BR");
    user.setUcode("4afd2390-4e3c-43cf-beba-8c8c053dd804");
    return user;
  }
}