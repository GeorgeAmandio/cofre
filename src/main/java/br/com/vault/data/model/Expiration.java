package br.com.vault.data.model;

public class Expiration {

  private Integer expireMonth;
  private Integer expireYear;

  public Expiration() {
  }  
  
  public Expiration(Integer expireMonth, Integer expireYear) {
    this.expireMonth = expireMonth;
    this.expireYear = expireYear;
  }

  public Integer getExpireMonth() {
    return expireMonth;
  }

  public void setExpireMonth(Integer expireMonth) {
    this.expireMonth = expireMonth;
  }

  public Integer getExpireYear() {
    return expireYear;
  }

  public void setExpireYear(Integer expireYear) {
    this.expireYear = expireYear;
  }
}
