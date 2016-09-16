package com.tempos21.mymarket.data.database.mapper;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.CountryEntity;
import com.tempos21.mymarket.sdk.model.Country;

public class CountryEntityMapper extends BaseEntityMapper<CountryEntity, Country> {

    public CountryEntityMapper(Context context) {
        super(context);
    }

    @Override
    public CountryEntity toEntity(Country model) {
        CountryEntity entity = new CountryEntity();
        entity.setId(model.id);
        entity.setName(model.name);
        return entity;
    }

    @Override
    public Country toModel(CountryEntity entity) {
        Country bo = new Country();
        bo.id = entity.getId();
        bo.name = entity.getName();
        return bo;
    }
}
