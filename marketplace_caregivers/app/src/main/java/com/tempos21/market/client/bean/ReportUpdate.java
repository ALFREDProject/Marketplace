package com.tempos21.market.client.bean;


public class ReportUpdate {

    private String id = "";
    private String appId = "";
    private String type = "";
    private String result = "";
    private int version = 0;


    public ReportUpdate() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String string) {
        this.appId = string;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


}
