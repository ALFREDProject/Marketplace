package com.tempos21.mymarket.data.database.mapper;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.LanguageEntity;
import com.tempos21.mymarket.sdk.model.Language;

public class LanguageEntityMapper extends BaseEntityMapper<LanguageEntity, Language> {

  public LanguageEntityMapper(Context context) {
    super(context);
  }

  @Override
  public LanguageEntity toEntity(Language model) {
    LanguageEntity entity = new LanguageEntity();
    entity.setId(model.id);
    entity.setName(model.name);
    return entity;
  }

  @Override
  public Language toModel(LanguageEntity entity) {
    Language bo = new Language();
    bo.id = entity.getId();
    bo.name = entity.getName();
    return bo;
  }
}
