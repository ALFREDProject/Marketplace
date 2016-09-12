package com.tempos21.mymarket.sdk.model;

public enum Platform {

  ANDROID(0, "Android");

  private int index;
  private String name;

  Platform(int index, String name) {
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