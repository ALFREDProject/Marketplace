package com.tempos21.mymarket.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tempos21.mymarket.sdk.model.app.Platform;

public class Token implements Parcelable {

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
    public String uuid;
    public Platform platforms;
    public User users;
    public int version;
    public int id;

    public Token() {
    }

    private Token(Parcel in) {
        this.uuid = in.readString();
        this.platforms = in.readParcelable(Platform.class.getClassLoader());
        this.users = in.readParcelable(User.class.getClassLoader());
        this.version = in.readInt();
        this.id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeParcelable(this.platforms, 0);
        dest.writeParcelable(this.users, 0);
        dest.writeInt(this.version);
        dest.writeInt(this.id);
    }
}
