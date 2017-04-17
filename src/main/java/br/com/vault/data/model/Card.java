package br.com.vault.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.vault.security.impl.CardIdGeneratorImpl;

import java.util.Date;
import org.springframework.data.annotation.Id;

public class Card {

  @Id
  private String id;

  private String cardId;
  private String number;
  private String type;
  private Expiration expiration;
  private Holder holder;
  private Integer cvv2;
  private Address billingAddress;
  private String externalCustomerId;
  private String merchantId;
  private String externalCardId;
  private Date createTime;
  private Date updateTime;
  private Date validUntil;

  @JsonIgnore
  private boolean deleted = false;

  @JsonIgnore
  private boolean listable = true;

  public Card() {
  }

  public Card(String externalCustomerId, String number, String type, Expiration expiration, Integer cvv2) {
    CardIdGeneratorImpl cardIdGenerator = new CardIdGeneratorImpl();

    this.externalCustomerId = externalCustomerId;
    this.number = number;
    this.type = type;
    this.expiration = expiration;
    this.cvv2 = cvv2;
    this.cardId = cardIdGenerator.generate();
  }

  public String getId() {
    return id;
  }

  public String getCardId() {
    return cardId;
  }
  
  public void setCardId(String cardId) {
	this.cardId = cardId;
  }

  public String getNumber() {
    return number;
  }

  public String getType() {
    return type;
  }

  public Expiration getExpiration() {
    return expiration;
  }

  public Integer getCvv2() {
    return cvv2;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public String getExternalCustomerId() {
    return externalCustomerId;
  }
  
  public void setExternalCustomerId(String externalCustomerId) {
	this.externalCustomerId = externalCustomerId;
  }

  public String getMerchantId() {
    return merchantId;
  }

  public void setMerchantId(String merchantId) {
    this.merchantId = merchantId;
  }

  public String getExternalCardId() {
    return externalCardId;
  }

  public void setExternalCardId(String externalCardId) {
    this.externalCardId = externalCardId;
  }

 public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(Date validUntil) {
    this.validUntil = validUntil;
  }

  public void delete() {
    this.deleted = true;
    this.updateTime = new Date();
  }

  public Holder getHolder() {
		return holder;
  }

  public void setHolder(Holder holder) {
	this.holder = holder;
  }  
  
}