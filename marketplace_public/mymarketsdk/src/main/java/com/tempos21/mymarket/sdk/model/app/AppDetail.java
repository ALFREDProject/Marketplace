package com.tempos21.mymarket.sdk.model.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AppDetail implements Parcelable {

  public Integer id;
  public String name;
  public String description;
  public List<String> screenshots = new ArrayList<String>();
  public String versionString;
  public String iconUrl;
  public Float rating;
  public String promoUrl;
  public Integer versionNumber;
  public String date;
  public Double size;
  public List<Platform> platform = new ArrayList<Platform>();
  public String notificationEmails;
  public Boolean allowed;
  public String supportEmails;
  public String author;
  public String externalUrl;
  public Integer versionId;
  public Boolean externalBinary;
  public String packageName;

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
    dest.writeString(this.description);
    dest.writeStringList(this.screenshots);
    dest.writeString(this.versionString);
    dest.writeString(this.iconUrl);
    dest.writeValue(this.rating);
    dest.writeString(this.promoUrl);
    dest.writeValue(this.versionNumber);
    dest.writeString(this.date);
    dest.writeValue(this.size);
    dest.writeTypedList(platform);
    dest.writeString(this.notificationEmails);
    dest.writeValue(this.allowed);
    dest.writeString(this.supportEmails);
    dest.writeString(this.author);
    dest.writeString(this.externalUrl);
    dest.writeValue(this.versionId);
    dest.writeValue(this.externalBinary);
    dest.writeString(this.packageName);
  }

  public AppDetail() {}

  protected AppDetail(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
    this.description = in.readString();
    this.screenshots = in.createStringArrayList();
    this.versionString = in.readString();
    this.iconUrl = in.readString();
    this.rating = (Float) in.readValue(Float.class.getClassLoader());
    this.promoUrl = in.readString();
    this.versionNumber = (Integer) in.readValue(Integer.class.getClassLoader());
    this.date = in.readString();
    this.size = (Double) in.readValue(Double.class.getClassLoader());
    this.platform = in.createTypedArrayList(Platform.CREATOR);
    this.notificationEmails = in.readString();
    this.allowed = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.supportEmails = in.readString();
    this.author = in.readString();
    this.externalUrl = in.readString();
    this.versionId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.externalBinary = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.packageName = in.readString();
  }

  public static final Parcelable.Creator<AppDetail> CREATOR = new Parcelable.Creator<AppDetail>() {
    public AppDetail createFromParcel(Parcel source) {return new AppDetail(source);}

    public AppDetail[] newArray(int size) {return new AppDetail[size];}
  };
}
