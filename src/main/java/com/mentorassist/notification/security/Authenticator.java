package com.mentorassist.notification.security;

import com.mentorassist.notification.vo.User;

public interface Authenticator {

  User authenticate(String token);
}
