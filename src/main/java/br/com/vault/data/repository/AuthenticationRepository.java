package br.com.vault.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.vault.data.model.Authentication;

@Repository
public interface AuthenticationRepository extends MongoRepository<Authentication, String> {

  Authentication findByApiKeyAndToken(String apiKey, String token);

  Authentication findByApiKey(String apiKey);
  
  void delete(Authentication authentication);
}
