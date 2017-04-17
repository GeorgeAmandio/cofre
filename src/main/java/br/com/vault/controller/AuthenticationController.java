package br.com.vault.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.vault.security.AuthenticationService;
import br.com.vault.security.exception.InvalidApiKeyException;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

  public AuthenticationService authenticationService;

  @Autowired
  private void setAuthenticationService(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @GetMapping(path = "/token", produces = MediaType.TEXT_HTML_VALUE)
  public String generateToken(@RequestHeader(value = "api-key") String apiKey) throws InvalidApiKeyException {
    return authenticationService.createSession(apiKey);
  }

  @ExceptionHandler(value = InvalidApiKeyException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid API key")
  public void invalidTokenHandler(InvalidApiKeyException e) {
  }
}
