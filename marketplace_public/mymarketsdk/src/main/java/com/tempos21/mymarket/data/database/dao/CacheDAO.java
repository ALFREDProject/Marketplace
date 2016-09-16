package com.tempos21.mymarket.data.database.dao;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.CacheEntity;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.constant.MyMarketConstants;

import io.realm.Realm;

public class CacheDAO extends BaseRealmDAO {

    public static final String FIELD_NAME = "name";

    public CacheDAO(Context context) {
        super(context);
    }

    public boolean updateCacheItem(String name, long timestamp) {
        try {
            Realm realm = Realm.getInstance(getContext());
            realm.beginTransaction();
            CacheEntity item = realm.where(CacheEntity.class).equalTo(FIELD_NAME, name).findFirst();
            item.setTimestamp(timestamp);
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

    public boolean putItemIntoCache(String name, long timestamp) {
        try {
            CacheEntity cacheEntity = new CacheEntity();
            cacheEntity.setName(name);
            cacheEntity.setTimestamp(timestamp);
            Realm realm = Realm.getInstance(getContext());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(cacheEntity);
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

    public boolean existsItemInCache(String name) {
        Realm realm = Realm.getInstance(getContext());
        CacheEntity result = realm.where(CacheEntity.class).equalTo(FIELD_NAME, name).findFirst();
        boolean exists = result != null;
        realm.close();
        return exists;
    }

    public boolean itemHasExpired(String name) {
        long timestamp = getItemTimestamp(name);
        long delta = System.currentTimeMillis() - timestamp;
        return delta > MyMarketConstants.EXPIRATION_DELTA;
    }

    public long getItemTimestamp(String name) {
        Realm realm = Realm.getInstance(getContext());
        CacheEntity result = realm.where(CacheEntity.class).equalTo(FIELD_NAME, name).findFirst();
        long timestamp = 0;
        if (result != null) {
            timestamp = result.getTimestamp();
            realm.close();
        }
        return timestamp;
    }
}
