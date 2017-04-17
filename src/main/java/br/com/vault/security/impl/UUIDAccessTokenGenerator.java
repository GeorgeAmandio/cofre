package br.com.vault.security.impl;

import java.util.UUID;

import br.com.vault.security.AccessTokenGenerator;

public class UUIDAccessTokenGenerator implements AccessTokenGenerator {

  @Override
  public String generate() {
    return UUID.randomUUID().toString();
  }
}
