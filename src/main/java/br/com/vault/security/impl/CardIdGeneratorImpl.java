package br.com.vault.security.impl;

import java.util.UUID;

import br.com.vault.security.CardIdGenerator;

public class CardIdGeneratorImpl implements CardIdGenerator {

  @Override
  public String generate() {
    return "cardId-" + UUID.randomUUID().toString();
  }
}
