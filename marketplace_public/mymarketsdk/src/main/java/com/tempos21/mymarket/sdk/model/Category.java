package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

  public long id;
  public String name;
  public String image;

  @Override
  public int describeContents() { return 0; }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
    dest.writeString(this.image);
  }

  public Category() {}

  private Category(Parcel in) {
    this.id = (long) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
    this.image = in.readString();
  }

  public static final Creator<Category> CREATOR = new Creator<Category>() {
    public Category createFromParcel(Parcel source) {return new Category(source);}

    public Category[] newArray(int size) {return new Category[size];}
  };
}
