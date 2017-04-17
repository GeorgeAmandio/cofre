package br.com.vault.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.vault.data.model.Address;
import br.com.vault.data.model.Application;
import br.com.vault.data.model.Card;
import br.com.vault.data.model.CardType;
import br.com.vault.data.model.CountryCode;
import br.com.vault.data.model.Expiration;
import br.com.vault.data.model.Holder;
import br.com.vault.data.repository.ApplicationRepository;
import br.com.vault.data.repository.exception.CardNotFoundException;
import br.com.vault.data.repository.impl.CardRepositoryImpl;
import br.com.vault.security.impl.CardIdGeneratorImpl;
import br.com.vault.security.impl.UUIDAccessTokenGenerator;
import br.com.vault.tools.CreditCardNumberGenerator;

@RestController
@RequestMapping(path = "/vault")
public class VaultController {

  private CardRepositoryImpl cardRepositoryImpl;
  
  @Autowired
  private ApplicationRepository applicationRepository;

  @Autowired
  public VaultController(CardRepositoryImpl cardRepositoryImpl) {
    this.cardRepositoryImpl = cardRepositoryImpl;
  }

  @GetMapping(path = "/cards")
  public List<Card> findByExternalCustomerId(@RequestParam("customerId") String externalCustomerId) {
    return cardRepositoryImpl.findByExternalCustomerId(externalCustomerId);
  }

  @GetMapping(path = "/cards/{cardId}")
  public Card findByCardId(@PathVariable("cardId") String cardId)
      throws CardNotFoundException {
    return cardRepositoryImpl.findByCardId(cardId);
  }

  @DeleteMapping(path = "/cards/{cardId}")
  public void deleteByCardId(@PathVariable("cardId") String cardId) throws CardNotFoundException {
    Card card = cardRepositoryImpl.findByCardId(cardId);
    card.delete();
    cardRepositoryImpl.update(card);
  }

  @GetMapping(path = "/cards/create")
  public void createCard() {
    CreditCardNumberGenerator creditCardNumberGenerator = new CreditCardNumberGenerator();
    
    Holder holder = new Holder();
    holder.setFirstName("Fulano");
    holder.setLastName("De Tal");

    Address billingAddress = new Address();
    billingAddress.setLine1("Rua Indiana 1165");
    billingAddress.setCountryCode(CountryCode.BRAZIL);
    billingAddress.setCity("Sao Paulo");
    billingAddress.setPostalCode("04562002");
    billingAddress.setState("SP");

    Card card = new Card("user23", creditCardNumberGenerator.generate("402455", 16), CardType.VISA,
        new Expiration(5, 2023), 105);
    card.setBillingAddress(billingAddress);
    card.setHolder(holder);
    cardRepositoryImpl.create(card);
  }

  @PostMapping(path = "/card/create")
  public void cardCreate(@RequestBody Card card, @RequestParam("customerId") String externalCustomerId) {
	  CardIdGeneratorImpl cardIdGenerator = new CardIdGeneratorImpl();
	  card.setExternalCustomerId(externalCustomerId);
	  card.setCardId(cardIdGenerator.generate());
	  card.setCreateTime(new Date());
	  cardRepositoryImpl.create(card);
  }

  @PostMapping(path = "/client/create")
  public void createClient(@RequestBody Application application) {
	  UUIDAccessTokenGenerator generator = new UUIDAccessTokenGenerator();
	  application.setApiKey(generator.generate());
	  applicationRepository.save(application);
  }

  @ExceptionHandler(value = CardNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid card ID")
  public void cardNotFoundHandler(CardNotFoundException e) {
  }
}
