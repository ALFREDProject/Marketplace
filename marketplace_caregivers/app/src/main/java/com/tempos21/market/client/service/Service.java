package com.tempos21.market.client.service;

import java.util.HashMap;

public interface Service {
    public String getUrl();

    public void setUrl(String url);

    public HashMap<String, String> getParameters();

    public HashMap<String, String> getEntityParameters();

    public HashMap<String, String> getRestParameters();
}
