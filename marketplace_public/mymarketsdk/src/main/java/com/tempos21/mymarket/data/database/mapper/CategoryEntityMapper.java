package com.tempos21.mymarket.data.database.mapper;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.CategoryEntity;
import com.tempos21.mymarket.sdk.model.Category;

public class CategoryEntityMapper extends BaseEntityMapper<CategoryEntity, Category> {

    public CategoryEntityMapper(Context context) {
        super(context);
    }

    @Override
    public CategoryEntity toEntity(Category model) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(model.id);
        entity.setName(model.name);
        return entity;
    }

    @Override
    public Category toModel(CategoryEntity entity) {
        Category bo = new Category();
        bo.id = entity.getId();
        bo.name = entity.getName();
        return bo;
    }
}
