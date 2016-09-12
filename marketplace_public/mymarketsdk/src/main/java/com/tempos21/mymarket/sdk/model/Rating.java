package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rating implements Parcelable {

  public String cache;
  public long id;
  public long userId;
  public String userName;
  public long rate;
  public String dateCreation;
  public String userFullName;
  public String versionString;
  public String text;
  public String title;

  public Rating() {
  }

  protected Rating(Parcel in) {
    cache = in.readString();
    id = in.readLong();
    userId = in.readLong();
    userName = in.readString();
    rate = in.readLong();
    dateCreation = in.readString();
    userFullName = in.readString();
    versionString = in.readString();
    text = in.readString();
    title = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(cache);
    dest.writeLong(id);
    dest.writeLong(userId);
    dest.writeString(userName);
    dest.writeLong(rate);
    dest.writeString(dateCreation);
    dest.writeString(userFullName);
    dest.writeString(versionString);
    dest.writeString(text);
    dest.writeString(title);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
    @Override
    public Rating createFromParcel(Parcel in) {
      return new Rating(in);
    }

    @Override
    public Rating[] newArray(int size) {
      return new Rating[size];
    }
  };

  @Override
  public String toString() {
    return "Rating:" +
            "\n  - cache: " + cache +
            "\n  - id: " + id +
            "\n  - userId: " + userId +
            "\n  - userName: " + userName +
            "\n  - rate: " + rate +
            "\n  - dateCreation: " + dateCreation +
            "\n  - userFullName: " + userFullName +
            "\n  - versionString: " + versionString +
            "\n  - text: " + text +
            "\n  - title: " + title;
  }
}