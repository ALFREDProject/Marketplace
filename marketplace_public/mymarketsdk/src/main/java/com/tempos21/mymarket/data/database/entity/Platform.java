package com.tempos21.mymarket.data.database.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Platform extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private Os os;

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

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }
}

