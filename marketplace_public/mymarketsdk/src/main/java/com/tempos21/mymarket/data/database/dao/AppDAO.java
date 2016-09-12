package com.tempos21.mymarket.data.database.dao;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.AppEntity;
import com.tempos21.mymarket.data.database.entity.AppListEntity;
import com.tempos21.mymarket.data.database.mapper.AppEntityMapper;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.mymarket.sdk.model.AppList;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class AppDAO extends BaseRealmDAO {

  private final AppEntityMapper mapper;

  public AppDAO(Context context) {
    super(context);
    mapper = new AppEntityMapper(context);
  }

  public boolean putList(AppList appList) {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      AppListEntity appListEntity = new AppListEntity();
      appListEntity.setName(appList.name);
      RealmList<AppEntity> appEntityRealmList = new RealmList<AppEntity>();
      for (App app : appList.appList) {
        appEntityRealmList.add(mapper.toEntity(app));
      }
      appListEntity.setAppList(appEntityRealmList);
      realm.copyToRealmOrUpdate(appListEntity);
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void appendList(AppList appList) {
    AppList newList = getList(appList.name);
    newList.appList.addAll(appList.appList);
    delete(appList.name);
    putList(newList);
  }

  public AppList getList(String name) {
    try {
      Realm realm = Realm.getInstance(getContext());
      AppListEntity result = realm.where(AppListEntity.class).equalTo("name", name).findFirst();
      if (result != null) {
        List<App> list = mapper.toModel(result.getAppList());
        realm.close();
        return new AppList(name, list);
      } else {
        realm.close();
        return null;
      }
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      return null;
    }
  }

  public boolean delete(String name) {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      realm.where(AppListEntity.class).equalTo("name", name).findAll().clear();
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      return false;
    }
  }
}
