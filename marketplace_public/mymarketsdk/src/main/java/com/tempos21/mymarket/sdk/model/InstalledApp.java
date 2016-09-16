package com.tempos21.mymarket.sdk.model;

/*
  {
    "change":
    [
      {"id":2,"type":"I","version":1},
      {"id":2,"type":"D","version":15},
      {"id":2,"type":"D","version":16},
      {"id":2,"type":"D","version":17},
      {"id":2,"type":"D","version":18}
    ]
  }
 */
public class InstalledApp {

    public static final String INSTALL = "I";
    public static final String DELETE = "D";

    public long id;
    public String type;
    public int version;
}
