package com.tempos21.mymarket.sdk.model;

public enum AppListSorting {

  RATING(0, "rating"),
  LATEST(1, "latest"),
  POPULAR(2, "popular");

  private int index;
  private String name;

  AppListSorting(int index, String name) {
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
