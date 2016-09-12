package com.tempos21.rampload.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class contains information to download and save the data to a path.
 */
public class RampLoadDownload implements Parcelable {

  public int id;
  public String name;
  public String url;
  public String path;

  public RampLoadDownload(int id, String name, String url, String path) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.path = path;
  }

  @Override
  public String toString() {
    return "RampLoadRequest {" +
      "id = '" + id + "'" +
      ", name = '" + name + "'" +
      ", url = '" + url + "'" +
      ", path = '" + path + "'" +
      '}';
  }

  public RampLoadDownload(Parcel in) {
    String[] data = new String[3];
    in.readStringArray(data);
    this.id = Integer.parseInt(data[0]);
    this.name = data[1];
    this.url = data[2];
    this.path = data[3];
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringArray(new String[]{"" + this.id, this.name, this.url, this.path});
  }

  public static final Creator CREATOR = new Creator() {
    @Override
    public RampLoadDownload createFromParcel(Parcel in) {
      return new RampLoadDownload(in);
    }

    @Override
    public RampLoadDownload[] newArray(int size) {
      return new RampLoadDownload[size];
    }
  };
}