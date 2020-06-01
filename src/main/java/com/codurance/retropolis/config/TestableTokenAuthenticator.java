package com.codurance.retropolis.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(Environment.TEST)
public class TestableTokenAuthenticator extends GoogleTokenAuthenticator {

  @Override
  public String getEmail(String token) {
    return "john.doe@codurance.com";
  }
}