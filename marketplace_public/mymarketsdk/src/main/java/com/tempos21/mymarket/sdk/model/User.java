package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

  public Integer id;
  public String name;
  public String dasUser;
  public String authToken;
  public Boolean isTester;
  public Boolean isApprover;

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
    dest.writeString(this.dasUser);
    dest.writeString(this.authToken);
    dest.writeValue(this.isTester);
    dest.writeValue(this.isApprover);
  }

  public User() {}

  private User(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
    this.dasUser = in.readString();
    this.authToken = in.readString();
    this.isTester = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.isApprover = (Boolean) in.readValue(Boolean.class.getClassLoader());
  }

  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    public User createFromParcel(Parcel source) {return new User(source);}

    public User[] newArray(int size) {return new User[size];}
  };
}
