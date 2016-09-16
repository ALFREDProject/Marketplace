package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.OsVO;
import com.tempos21.mymarket.sdk.model.app.Os;

import java.lang.reflect.Type;

public class OsVOMapper extends BaseVOMapper<OsVO, Os> {

    public static ClientResponse<OsVO> mapResponse(String json) {
        Type type = new TypeToken<ClientResponse<OsVO>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public OsVO toVO(Os model) {
        return null;
    }

    @Override
    public Os toModel(OsVO vo) {
        Os os = new Os();
        os.id = vo.id;
        os.name = vo.name;
        os.extension = vo.extension;
        return os;
    }
}
