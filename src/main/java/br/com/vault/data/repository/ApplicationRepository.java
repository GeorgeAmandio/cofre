package br.com.vault.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.vault.data.model.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {

  Application findByApiKey(String apiKey);
  
  void delete(Application application);
}
