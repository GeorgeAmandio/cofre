package br.com.vault.data.repository.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import br.com.vault.data.model.Card;
import br.com.vault.data.repository.CardRepository;
import br.com.vault.data.repository.exception.CardNotFoundException;

@Repository
public class CardRepositoryImpl implements CardRepository {

  private MongoTemplate mongoTemplate;

  @Autowired
  public void setMongoTemplate(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Card findByCardId(String cardId) throws CardNotFoundException {
    Query query = new Query().addCriteria(
        Criteria.where("cardId").is(cardId).and("deleted").is(false).and("listable").is(true));
    Card card = mongoTemplate.findOne(query, Card.class);
    if (card == null) {
      throw new CardNotFoundException();
    }
    return card;
  }

  public List<Card> findByExternalCustomerId(String externalCustomerId) {
    Query query = new Query();
    query.addCriteria(
        Criteria.where("externalCustomerId").is(externalCustomerId).and("deleted").is(false)
            .and("listable").is(true));
    return mongoTemplate.find(query, Card.class);
  }

  public void create(Card card) {
    mongoTemplate.insert(card);
  }

  public void update(Card card) {
    card.delete();
    mongoTemplate.save(card);
  }
}
