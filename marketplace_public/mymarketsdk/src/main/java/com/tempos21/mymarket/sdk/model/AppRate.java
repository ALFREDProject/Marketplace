package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AppRate implements Parcelable {

  public long userId;
  public String versionString;
  public String text;
  public String dateCreation;
  public String userFullName;
  public String title;
  public int rate;
  public String userName;
  public long id;

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(this.userId);
    dest.writeString(this.versionString);
    dest.writeString(this.text);
    dest.writeString(this.dateCreation);
    dest.writeString(this.userFullName);
    dest.writeString(this.title);
    dest.writeInt(this.rate);
    dest.writeString(this.userName);
    dest.writeLong(this.id);
  }

  public AppRate() {}

  protected AppRate(Parcel in) {
    this.userId = in.readLong();
    this.versionString = in.readString();
    this.text = in.readString();
    this.dateCreation = in.readString();
    this.userFullName = in.readString();
    this.title = in.readString();
    this.rate = in.readInt();
    this.userName = in.readString();
    this.id = in.readLong();
  }

  public static final Parcelable.Creator<AppRate> CREATOR = new Parcelable.Creator<AppRate>() {
    public AppRate createFromParcel(Parcel source) {return new AppRate(source);}

    public AppRate[] newArray(int size) {return new AppRate[size];}
  };
}
