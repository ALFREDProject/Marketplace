package com.tempos21.mymarket.data.database.mapper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public abstract class BaseEntityMapper<E extends RealmObject, M> implements EntityMapper<E, M> {

    private final Context context;

    public BaseEntityMapper(Context context) {
        this.context = context;
    }

    @Override
    public List<E> toEntity(List<M> modelList) {
        List<E> entityList = new ArrayList<>(modelList.size());
        for (M model : modelList) {
            entityList.add(toEntity(model));
        }
        return entityList;
    }

    @Override
    public List<M> toModel(List<E> entityList) {
        List<M> modelList = new ArrayList<>(entityList.size());
        for (E entity : entityList) {
            modelList.add(toModel(entity));
        }
        return modelList;
    }

    public Context getContext() {
        return context;
    }
}
