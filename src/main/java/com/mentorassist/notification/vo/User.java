package com.mentorassist.notification.vo;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class User {

  private Long id;
  private String ucode;
  private String name;
  private String email;
  private String login;
  private String locale;
  private String country;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUcode() {
    return ucode;
  }

  public void setUcode(String ucode) {
    this.ucode = ucode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
