package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.PlatformVO;
import com.tempos21.mymarket.sdk.model.app.Platform;

import java.lang.reflect.Type;

public class PlatformVOMapper extends BaseVOMapper<PlatformVO, Platform> {

  public static ClientResponse<PlatformVO> mapResponse(String json) {
    Type type = new TypeToken<ClientResponse<PlatformVO>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  @Override
  public Platform toModel(PlatformVO vo) {
    Platform platform = new Platform();
    platform.id = vo.id;
    platform.name = vo.name;
    platform.os = new OsVOMapper().toModel(vo.os);
    return platform;
  }

  @Override
  public PlatformVO toVO(Platform model) {
    return null;
  }
}