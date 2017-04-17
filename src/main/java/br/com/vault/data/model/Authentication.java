package br.com.vault.data.model;

import java.io.Serializable;
import org.springframework.data.annotation.Id;

@SuppressWarnings("serial")
public class Authentication implements Serializable {

  @Id
  private String id;

  private String apiKey;
  private String token;

  public Authentication(String apiKey, String token) {
    this.apiKey = apiKey;
    this.token = token;
  }

  public String getId() {
    return id;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getToken() {
    return token;
  }
}
