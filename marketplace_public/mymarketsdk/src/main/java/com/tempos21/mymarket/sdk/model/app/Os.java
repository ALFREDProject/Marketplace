package com.tempos21.mymarket.sdk.model.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Os implements Parcelable {

    public static final Parcelable.Creator<Os> CREATOR = new Parcelable.Creator<Os>() {
        public Os createFromParcel(Parcel source) {
            return new Os(source);
        }

        public Os[] newArray(int size) {
            return new Os[size];
        }
    };
    public long id;
    public String name;
    public String extension;

    public Os() {
    }

    protected Os(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.extension = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.extension);
    }
}
