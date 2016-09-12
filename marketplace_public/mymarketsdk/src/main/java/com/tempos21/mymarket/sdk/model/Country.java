package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {

  public long id;
  public String name;

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
  }

  public Country() {}

  private Country(Parcel in) {
    this.id = (long) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
  }

  public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
    public Country createFromParcel(Parcel source) {return new Country(source);}

    public Country[] newArray(int size) {return new Country[size];}
  };
}
