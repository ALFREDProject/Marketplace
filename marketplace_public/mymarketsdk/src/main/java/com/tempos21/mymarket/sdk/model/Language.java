package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Language implements Parcelable {

  public long id;
  public String name;

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
  }

  public Language() {}

  private Language(Parcel in) {
    this.id = (long) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
  }

  public static final Creator<Language> CREATOR = new Creator<Language>() {
    public Language createFromParcel(Parcel source) {return new Language(source);}

    public Language[] newArray(int size) {return new Language[size];}
  };
}
