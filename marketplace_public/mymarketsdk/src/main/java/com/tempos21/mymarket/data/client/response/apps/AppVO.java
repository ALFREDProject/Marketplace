package com.tempos21.mymarket.data.client.response.apps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AppVO {

    @SerializedName("version_number")
    @Expose
    public Integer versionNumber;
    @Expose
    public Boolean allowed;
    @Expose
    public String supportEmails;
    @SerializedName("version_string")
    @Expose
    public String versionString;
    @SerializedName("icon_url")
    @Expose
    public String iconUrl;
    @Expose
    public Float rating;
    @SerializedName("promo_url")
    @Expose
    public String promoUrl;
    @Expose
    public String author;
    @Expose
    public String externalUrl;
    @SerializedName("version_id")
    @Expose
    public Integer versionId;
    @Expose
    public Boolean externalBinary;
    @Expose
    public List<PlatformVO> platform = new ArrayList<PlatformVO>();
    @Expose
    public String notificationEmails;
    @Expose
    public String packageName;
    @Expose
    public String date;
    @Expose
    public String name;
    @Expose
    public Integer id;
}
