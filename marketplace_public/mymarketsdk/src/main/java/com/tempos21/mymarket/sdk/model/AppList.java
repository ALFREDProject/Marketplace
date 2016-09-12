package com.tempos21.mymarket.sdk.model;

import com.tempos21.mymarket.sdk.model.app.App;

import java.util.List;

public class AppList {

  public String name;
  public List<App> appList;

  public AppList(String name, List<App> appList) {
    this.name = name;
    this.appList = appList;
  }
}
