package br.com.vault.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.vault.data.model.Application;
import br.com.vault.data.model.Authentication;
import br.com.vault.data.repository.ApplicationRepository;
import br.com.vault.data.repository.AuthenticationRepository;
import br.com.vault.security.exception.AuthenticationException;
import br.com.vault.security.exception.InvalidApiKeyException;
import br.com.vault.security.impl.UUIDAccessTokenGenerator;

@Component
public class AuthenticationService {

  private static final Logger logger = Logger.getLogger(AuthenticationService.class);

  private AuthenticationRepository authenticationRepository;
  private ApplicationRepository applicationRepository;

  @Autowired
  private void setAuthenticationRepository(AuthenticationRepository authenticationRepository) {
    this.authenticationRepository = authenticationRepository;
  }

  @Autowired
  private void setApplicationRepository(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  public String createSession(String apiKey) throws InvalidApiKeyException {
    Application application = applicationRepository.findByApiKey(apiKey);
    String token;
    if (application == null) {
      throw new InvalidApiKeyException();
    } else {
      invalidateSession(apiKey);
      token = generateToken();
      Authentication authentication = new Authentication(apiKey, token);
      authenticationRepository.save(authentication);
    }
    return token;
  }

  public void authenticate(String apiKey, String token) throws AuthenticationException {
    Authentication authentication = authenticationRepository.findByApiKeyAndToken(apiKey, token);
    if (authentication == null) {
      logger.info("================ Authentication Failed! ================");
      throw new AuthenticationException();
    }
  }

  private void invalidateSession(String apiKey) {
    Authentication authentication = getCurrentSessionForApiKey(apiKey);
    if (authentication != null) {
      authenticationRepository.delete(authentication.getId());
    }
  }

  private Authentication getCurrentSessionForApiKey(String apiKey) {
    return authenticationRepository.findByApiKey(apiKey);
  }

  private String generateToken() {
    AccessTokenGenerator tokenGenerator = new UUIDAccessTokenGenerator();
    return tokenGenerator.generate();
  }
}
