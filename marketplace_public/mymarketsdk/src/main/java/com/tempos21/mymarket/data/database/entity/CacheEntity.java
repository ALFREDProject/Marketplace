package com.tempos21.mymarket.data.database.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CacheEntity extends RealmObject {

  @PrimaryKey
  private String name;
  private long timestamp;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
