package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.app_rate.AppRateListVO;
import com.tempos21.mymarket.data.client.response.app_rate.AppRateVO;
import com.tempos21.mymarket.sdk.model.AppRate;

import java.lang.reflect.Type;

public class AppRateVOMapper extends BaseVOMapper<AppRateVO, AppRate> {

    public static ClientResponse<AppRateListVO> mapResponse(String json) {
        Type type = new TypeToken<ClientResponse<AppRateListVO>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public AppRateVO toVO(AppRate model) {
        return null;
    }

    @Override
    public AppRate toModel(AppRateVO vo) {
        AppRate appRate = new AppRate();
        appRate.userId = vo.userId;
        appRate.versionString = vo.versionString;
        appRate.text = vo.text;
        appRate.dateCreation = vo.dateCreation;
        appRate.userFullName = vo.userFullName;
        appRate.title = vo.title;
        appRate.rate = vo.rate;
        appRate.userName = vo.userName;
        appRate.id = vo.id;
        return appRate;
    }
}
