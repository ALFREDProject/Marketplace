package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.language.LanguageListVO;
import com.tempos21.mymarket.data.client.response.language.LanguageVO;
import com.tempos21.mymarket.sdk.model.Language;

import java.lang.reflect.Type;

public class LanguageVOMapper extends BaseVOMapper<LanguageVO, Language> {

  public static ClientResponse<LanguageListVO> mapResponse(String json) {
    Type type = new TypeToken<ClientResponse<LanguageListVO>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  @Override
  public Language toModel(LanguageVO vo) {
    Language language = new Language();
    language.id = vo.id;
    language.name = vo.name;
    return language;
  }

  @Override
  public LanguageVO toVO(Language model) {
    return null;
  }
}
