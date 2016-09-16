package com.tempos21.mymarket.data.database.mapper;

import android.content.Context;

import com.tempos21.mymarket.data.database.entity.AppEntity;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.mymarket.sdk.model.app.Os;
import com.tempos21.mymarket.sdk.model.app.Platform;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class AppEntityMapper extends BaseEntityMapper<AppEntity, App> {

    public AppEntityMapper(Context context) {
        super(context);
    }

    @Override
    public AppEntity toEntity(App model) {
        AppEntity entity = new AppEntity();
        entity.setId(model.id);
        entity.setName(model.name);
        entity.setVersionNumber(model.versionNumber);
        entity.setAllowed(model.allowed);
        entity.setSupportEmails(model.supportEmails);
        entity.setVersionString(model.versionString);
        entity.setIconUrl(model.iconUrl);
        entity.setRating(model.rating);
        entity.setPromoUrl(model.promoUrl);
        entity.setAuthor(model.author);
        entity.setExternalUrl(model.externalUrl);
        entity.setVersionId(model.versionId);
        entity.setExternalBinary(model.externalBinary);
        entity.setPlatform(getPlatformRealmList(model.platform));
        entity.setNotificationEmails(model.notificationEmails);
        entity.setPackageName(model.packageName);
        entity.setDate(model.date);
        return entity;
    }

    @Override
    public App toModel(AppEntity entity) {
        App bo = new App();
        bo.id = entity.getId();
        bo.name = entity.getName();
        bo.versionNumber = entity.getVersionNumber();
        bo.allowed = entity.getAllowed();
        bo.supportEmails = entity.getSupportEmails();
        bo.versionString = entity.getVersionString();
        bo.iconUrl = entity.getIconUrl();
        bo.rating = entity.getRating();
        bo.promoUrl = entity.getPromoUrl();
        bo.author = entity.getAuthor();
        bo.externalUrl = entity.getExternalUrl();
        bo.versionId = entity.getVersionId();
        bo.externalBinary = entity.getExternalBinary();
        bo.platform = getPlatformList(entity.getPlatform());
        bo.notificationEmails = entity.getNotificationEmails();
        bo.packageName = entity.getPackageName();
        bo.date = entity.getDate();
        return bo;
    }

    private RealmList<com.tempos21.mymarket.data.database.entity.Platform> getPlatformRealmList(List<Platform> platform) {
        RealmList<com.tempos21.mymarket.data.database.entity.Platform> list = new RealmList<com.tempos21.mymarket.data.database.entity.Platform>();
        com.tempos21.mymarket.data.database.entity.Platform realmPlatform;
        for (Platform p : platform) {
            realmPlatform = new com.tempos21.mymarket.data.database.entity.Platform();
            realmPlatform.setId(p.id);
            realmPlatform.setName(p.name);
            realmPlatform.setOs(getRealmOs(p.os));
            list.add(realmPlatform);
        }
        return list;
    }

    private com.tempos21.mymarket.data.database.entity.Os getRealmOs(Os os) {
        com.tempos21.mymarket.data.database.entity.Os realmOs = new com.tempos21.mymarket.data.database.entity.Os();
        realmOs.setId(os.id);
        realmOs.setName(os.name);
        realmOs.setExtension(os.extension);
        return realmOs;
    }

    private List<Platform> getPlatformList(RealmList<com.tempos21.mymarket.data.database.entity.Platform> platform) {
        List<Platform> list = new ArrayList<Platform>();
        Platform modelPlatform;
        for (com.tempos21.mymarket.data.database.entity.Platform p : platform) {
            modelPlatform = new Platform();
            modelPlatform.id = p.getId();
            modelPlatform.name = p.getName();
            modelPlatform.os = getOs(p.getOs());
            list.add(modelPlatform);
        }
        return list;
    }

    private Os getOs(com.tempos21.mymarket.data.database.entity.Os os) {
        Os modelOs = new Os();
        modelOs.id = os.getId();
        modelOs.name = os.getName();
        modelOs.extension = os.getExtension();
        return modelOs;
    }
}
