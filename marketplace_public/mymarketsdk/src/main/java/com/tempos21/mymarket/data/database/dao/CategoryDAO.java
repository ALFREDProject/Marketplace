package com.tempos21.mymarket.data.database.dao;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.CategoryEntity;
import com.tempos21.mymarket.data.database.mapper.CategoryEntityMapper;
import com.tempos21.mymarket.sdk.model.Category;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryDAO extends BaseRealmDAO {

    private final CategoryEntityMapper mapper;

    public CategoryDAO(Context context) {
        super(context);
        mapper = new CategoryEntityMapper(context);
    }

    public boolean put(Category category) {
        try {
            Realm realm = Realm.getInstance(getContext());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(mapper.toEntity(category));
            realm.commitTransaction();
            realm.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean putList(List<Category> categoryList) {
        try {
            Realm realm = Realm.getInstance(getContext());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(mapper.toEntity(categoryList));
            realm.commitTransaction();
            realm.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Category get(long id) {
        Realm realm = Realm.getInstance(getContext());
        CategoryEntity result = realm.where(CategoryEntity.class).equalTo("id", id).findFirst();
        if (result != null) {
            Category category = mapper.toModel(result);
            realm.close();
            return category;
        } else {
            realm.close();
            return null;
        }
    }

    public List<Category> getList() {
        Realm realm = Realm.getInstance(getContext());
        RealmResults<CategoryEntity> result = realm.where(CategoryEntity.class).findAll();
        if (result != null) {
            List<Category> list = mapper.toModel(result);
            realm.close();
            return list;
        } else {
            realm.close();
            return new ArrayList<Category>();
        }
    }

    public boolean clearAll() {
        try {
            Realm realm = Realm.getInstance(getContext());
            realm.beginTransaction();
            realm.where(CategoryEntity.class).findAll().clear();
            realm.commitTransaction();
            realm.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
