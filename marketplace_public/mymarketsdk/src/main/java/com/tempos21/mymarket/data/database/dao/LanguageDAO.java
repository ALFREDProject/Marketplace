package com.tempos21.mymarket.data.database.dao;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.LanguageEntity;
import com.tempos21.mymarket.data.database.mapper.LanguageEntityMapper;
import com.tempos21.mymarket.sdk.model.Language;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class LanguageDAO extends BaseRealmDAO {

  private final LanguageEntityMapper mapper;

  public LanguageDAO(Context context) {
    super(context);
    mapper = new LanguageEntityMapper(context);
  }

  public boolean put(Language language) {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(mapper.toEntity(language));
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean putList(List<Language> languageList) {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(mapper.toEntity(languageList));
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Language get(long id) {
    Realm realm = Realm.getInstance(getContext());
    LanguageEntity result = realm.where(LanguageEntity.class).equalTo("id", id).findFirst();
    if (result != null) {
      Language language = mapper.toModel(result);
      realm.close();
      return language;
    } else {
      realm.close();
      return null;
    }
  }

  public List<Language> getList() {
    Realm realm = Realm.getInstance(getContext());
    RealmResults<LanguageEntity> result = realm.where(LanguageEntity.class).findAll();
    if (result != null) {
      List<Language> list = mapper.toModel(result);
      realm.close();
      return list;
    } else {
      realm.close();
      return new ArrayList<Language>();
    }
  }

  public boolean clearAll() {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      RealmResults<LanguageEntity> result = realm.where(LanguageEntity.class).findAll();
      result.clear();
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
