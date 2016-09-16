package com.tempos21.mymarket.sdk.model.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class App implements Parcelable {

    public static final Parcelable.Creator<App> CREATOR = new Parcelable.Creator<App>() {
        public App createFromParcel(Parcel source) {
            return new App(source);
        }

        public App[] newArray(int size) {
            return new App[size];
        }
    };
    public long id;
    public String name;
    public Integer versionNumber;
    public Boolean allowed;
    public String supportEmails;
    public String versionString;
    public String iconUrl;
    public Float rating;
    public String promoUrl;
    public String author;
    public String externalUrl;
    public Integer versionId;
    public Boolean externalBinary;
    public List<Platform> platform = new ArrayList<Platform>();
    public String notificationEmails;
    public String packageName;
    public String date;

    public App() {
    }

    protected App(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.versionNumber = (Integer) in.readValue(Integer.class.getClassLoader());
        this.allowed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.supportEmails = in.readString();
        this.versionString = in.readString();
        this.iconUrl = in.readString();
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
        this.promoUrl = in.readString();
        this.author = in.readString();
        this.externalUrl = in.readString();
        this.versionId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.externalBinary = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.platform = in.createTypedArrayList(Platform.CREATOR);
        this.notificationEmails = in.readString();
        this.packageName = in.readString();
        this.date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.versionNumber);
        dest.writeValue(this.allowed);
        dest.writeString(this.supportEmails);
        dest.writeString(this.versionString);
        dest.writeString(this.iconUrl);
        dest.writeValue(this.rating);
        dest.writeString(this.promoUrl);
        dest.writeString(this.author);
        dest.writeString(this.externalUrl);
        dest.writeValue(this.versionId);
        dest.writeValue(this.externalBinary);
        dest.writeTypedList(platform);
        dest.writeString(this.notificationEmails);
        dest.writeString(this.packageName);
        dest.writeString(this.date);
    }
}
