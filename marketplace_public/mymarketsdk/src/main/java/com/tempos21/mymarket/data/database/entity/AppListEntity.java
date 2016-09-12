package com.tempos21.mymarket.data.database.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppListEntity extends RealmObject {

  @PrimaryKey
  private String name;
  private RealmList<AppEntity> appList = new RealmList<AppEntity>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RealmList<AppEntity> getAppList() {
    return appList;
  }

  public void setAppList(RealmList<AppEntity> appList) {
    this.appList = appList;
  }
}
