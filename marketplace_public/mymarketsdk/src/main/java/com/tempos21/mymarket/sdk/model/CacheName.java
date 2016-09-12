package com.tempos21.mymarket.sdk.model;

public enum CacheName {

  CATEGORY(0, "categoryCache"),
  COUNTRY(1, "countryCache"),
  LANGUAGE(2, "languageCache"),
  RATING(3, "ratingCache");

  private int index;
  private String name;

  CacheName(int index, String name) {
    this.index = index;
    this.name = name;
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }
}
