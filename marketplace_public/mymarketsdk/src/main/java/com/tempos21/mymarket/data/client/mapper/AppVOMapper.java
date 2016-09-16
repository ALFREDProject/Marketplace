package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.AppListVO;
import com.tempos21.mymarket.data.client.response.apps.AppVO;
import com.tempos21.mymarket.sdk.model.app.App;

import java.lang.reflect.Type;

public class AppVOMapper extends BaseVOMapper<AppVO, App> {

    public static ClientResponse<AppListVO> mapResponse(String json) {
        Type type = new TypeToken<ClientResponse<AppListVO>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public AppVO toVO(App model) {
        return null;
    }

    @Override
    public App toModel(AppVO vo) {
        App app = new App();
        app.id = vo.id;
        app.name = vo.name;
        app.versionNumber = vo.versionNumber;
        app.allowed = vo.allowed;
        app.supportEmails = vo.supportEmails;
        app.versionString = vo.versionString;
        app.iconUrl = vo.iconUrl;
        app.rating = vo.rating;
        app.promoUrl = vo.promoUrl;
        app.author = vo.author;
        app.externalUrl = vo.externalUrl;
        app.versionId = vo.versionId;
        app.externalBinary = vo.externalBinary;
        app.platform = new PlatformVOMapper().toModel(vo.platform);
        app.notificationEmails = vo.notificationEmails;
        app.packageName = vo.packageName;
        app.date = vo.date;
        return app;
    }
}
