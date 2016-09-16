package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.AppDetailVO;
import com.tempos21.mymarket.sdk.model.app.AppDetail;

import java.lang.reflect.Type;

public class AppDetailVOMapper extends BaseVOMapper<AppDetailVO, AppDetail> {

    public static ClientResponse<AppDetailVO> mapResponse(String json) {
        Type type = new TypeToken<ClientResponse<AppDetailVO>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    @Override
    public AppDetailVO toVO(AppDetail model) {
        return null;
    }

    @Override
    public AppDetail toModel(AppDetailVO vo) {
        AppDetail detail = new AppDetail();
        detail.id = vo.id;
        detail.name = vo.name;
        detail.description = vo.description;
        detail.screenshots = vo.screenshots;
        detail.versionString = vo.versionString;
        detail.iconUrl = vo.iconUrl;
        detail.rating = vo.rating;
        detail.promoUrl = vo.promoUrl;
        detail.versionNumber = vo.versionNumber;
        detail.date = vo.date;
        detail.size = vo.size;
        detail.platform = new PlatformVOMapper().toModel(vo.platform);
        detail.notificationEmails = vo.notificationEmails;
        detail.allowed = vo.allowed;
        detail.supportEmails = vo.supportEmails;
        detail.author = vo.author;
        detail.externalUrl = vo.externalUrl;
        detail.versionId = vo.versionId;
        detail.externalBinary = vo.externalBinary;
        detail.packageName = vo.packageName;
        return detail;
    }
}
