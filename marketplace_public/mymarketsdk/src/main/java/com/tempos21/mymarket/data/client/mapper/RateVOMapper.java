package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.app_rate.RateVO;
import com.tempos21.mymarket.sdk.model.Rate;

import java.lang.reflect.Type;

public class RateVOMapper extends BaseVOMapper<RateVO, Rate> {

    public static ClientResponse<RateVO> mapResponse(String json) {
        Type type = new TypeToken<ClientResponse<RateVO>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public RateVO toVO(Rate model) {
        return null;
    }

    @Override
    public Rate toModel(RateVO vo) {
        Rate rate = new Rate();
        rate.versionString = vo.versionString;
        rate.score = vo.score;
        rate.subject = vo.subject;
        rate.date = vo.date;
        rate.id = vo.id;
        rate.comment = vo.comment;
        rate.users = new UserVOMapper().toModel(vo.users);
        return rate;
    }
}
