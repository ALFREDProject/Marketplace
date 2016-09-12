package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.set_installed_apps.DatumVO;

import java.lang.reflect.Type;
import java.util.List;

public class InstalledVOMapper {

  public static ClientResponse<List<DatumVO>> mapResponse(String json) {
    Type type = new TypeToken<ClientResponse<List<DatumVO>>>() {}.getType();
    return new Gson().fromJson(json, type);
  }
}
