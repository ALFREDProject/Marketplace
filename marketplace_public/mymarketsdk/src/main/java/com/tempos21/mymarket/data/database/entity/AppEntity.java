package com.tempos21.mymarket.data.database.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AppEntity extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private int versionNumber;
    private boolean allowed;
    private String supportEmails;
    private String versionString;
    private String iconUrl;
    private float rating;
    private String promoUrl;
    private String author;
    private String externalUrl;
    private int versionId;
    private boolean externalBinary;
    private RealmList<Platform> platform = new RealmList<Platform>();
    private String notificationEmails;
    private String packageName;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public boolean getAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getSupportEmails() {
        return supportEmails;
    }

    public void setSupportEmails(String supportEmails) {
        this.supportEmails = supportEmails;
    }

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPromoUrl() {
        return promoUrl;
    }

    public void setPromoUrl(String promoUrl) {
        this.promoUrl = promoUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public boolean getExternalBinary() {
        return externalBinary;
    }

    public void setExternalBinary(boolean externalBinary) {
        this.externalBinary = externalBinary;
    }

    public RealmList<Platform> getPlatform() {
        return platform;
    }

    public void setPlatform(RealmList<Platform> platform) {
        this.platform = platform;
    }

    public String getNotificationEmails() {
        return notificationEmails;
    }

    public void setNotificationEmails(String notificationEmails) {
        this.notificationEmails = notificationEmails;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
