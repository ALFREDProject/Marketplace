package com.tempos21.mymarket.data.client.response.apps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AppDetailVO {

  @Expose
  public String description;
  @Expose
  public List<String> screenshots = new ArrayList<String>();
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
  @SerializedName("version_number")
  @Expose
  public Integer versionNumber;
  @Expose
  public String date;
  @Expose
  public String name;
  @Expose
  public Integer id;
  @Expose
  public Double size;
  @Expose
  public List<PlatformVO> platform = new ArrayList<PlatformVO>();
  @Expose
  public String notificationEmails;
  @Expose
  public Boolean allowed;
  @Expose
  public String supportEmails;
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
  public String packageName;
}
