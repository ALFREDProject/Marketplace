package com.tempos21.market.client.bean;

public class Country implements Comparable<Country> {

    // JSONObject & DB fields

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Country another) {
        return name.compareTo(another.getName());
    }

}
