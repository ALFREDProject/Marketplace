package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rate implements Parcelable {

    public static final Parcelable.Creator<Rate> CREATOR = new Parcelable.Creator<Rate>() {
        public Rate createFromParcel(Parcel source) {
            return new Rate(source);
        }

        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };
    public String versionString;
    public int score;
    public String subject;
    public long date;
    public int id;
    public String comment;
    public User users;

    public Rate() {
    }

    private Rate(Parcel in) {
        this.versionString = in.readString();
        this.score = in.readInt();
        this.subject = in.readString();
        this.date = in.readLong();
        this.id = in.readInt();
        this.comment = in.readString();
        this.users = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionString);
        dest.writeInt(this.score);
        dest.writeString(this.subject);
        dest.writeLong(this.date);
        dest.writeInt(this.id);
        dest.writeString(this.comment);
        dest.writeParcelable(this.users, 0);
    }
}
