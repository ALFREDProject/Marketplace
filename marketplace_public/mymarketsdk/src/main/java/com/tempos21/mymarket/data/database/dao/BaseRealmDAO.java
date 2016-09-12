package com.tempos21.mymarket.data.database.dao;

import android.content.Context;

public class BaseRealmDAO {

  private final Context context;

  public BaseRealmDAO(Context context) {
    this.context = context;
  }

  public Context getContext() {
    return context;
  }
}
