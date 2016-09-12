package com.tempos21.market.client.bean;

public class Transaction {

    private String type;
    private String id;
    private int version;


    public Transaction(String id, String type, int version) {
        super();
        this.type = type;
        this.id = id;
        this.version = version;
    }

    public Transaction() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
