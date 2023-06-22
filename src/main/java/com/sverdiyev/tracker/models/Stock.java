package com.sverdiyev.tracker.models;

public class Stock {

  private final String name;
  private final String ticker;

  public Stock(String name, String ticker) {
    this.name = name;
    this.ticker = ticker;
  }

  public String getName() {
    return name;
  }

  public String getTicker() {
    return ticker;
  }
}
