package com.tempos21.mymarket.sdk.model.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Platform implements Parcelable {

    public static final Parcelable.Creator<Platform> CREATOR = new Parcelable.Creator<Platform>() {
        public Platform createFromParcel(Parcel source) {
            return new Platform(source);
        }

        public Platform[] newArray(int size) {
            return new Platform[size];
        }
    };
    public long id;
    public String name;
    public Os os;

    public Platform() {
    }

    protected Platform(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.os = in.readParcelable(com.tempos21.mymarket.data.database.entity.Os.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.os, flags);
    }
}

