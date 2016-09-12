package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.country.CountryData;
import com.tempos21.mymarket.data.client.response.country.CountryVO;
import com.tempos21.mymarket.sdk.model.Country;

import java.lang.reflect.Type;

public class CountryVOMapper extends BaseVOMapper<CountryVO, Country> {

  public static ClientResponse<CountryData> mapResponse(String json) {
    Type type = new TypeToken<ClientResponse<CountryData>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  @Override
  public Country toModel(CountryVO vo) {
    Country country = new Country();
    country.id = vo.id;
    country.name = vo.name;
    return country;
  }

  @Override
  public CountryVO toVO(Country model) {
    return null;
  }
}
