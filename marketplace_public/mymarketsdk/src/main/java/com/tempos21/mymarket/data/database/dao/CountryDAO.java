package com.tempos21.mymarket.data.database.dao;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.CountryEntity;
import com.tempos21.mymarket.data.database.mapper.CountryEntityMapper;
import com.tempos21.mymarket.sdk.model.Country;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CountryDAO extends BaseRealmDAO {

  private final CountryEntityMapper mapper;

  public CountryDAO(Context context) {
    super(context);
    mapper = new CountryEntityMapper(context);
  }

  public boolean put(Country country) {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(mapper.toEntity(country));
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean putList(List<Country> countryList) {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      realm.copyToRealmOrUpdate(mapper.toEntity(countryList));
      realm.commitTransaction();
      realm.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Country get(long id) {
    Realm realm = Realm.getInstance(getContext());
    CountryEntity result = realm.where(CountryEntity.class).equalTo("id", id).findFirst();
    if (result != null) {
      Country country = mapper.toModel(result);
      realm.close();
      return country;
    } else {
      realm.close();
      return null;
    }
  }

  public List<Country> getList() {
    Realm realm = Realm.getInstance(getContext());
    RealmResults<CountryEntity> result = realm.where(CountryEntity.class).findAll();
    if (result != null) {
      List<Country> list = mapper.toModel(result);
      realm.close();
      return list;
    } else {
      realm.close();
      return new ArrayList<Country>();
    }
  }

  public boolean clearAll() {
    try {
      Realm realm = Realm.getInstance(getContext());
      realm.beginTransaction();
      RealmResults<CountryEntity> result = realm.where(CountryEntity.class).findAll();
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
