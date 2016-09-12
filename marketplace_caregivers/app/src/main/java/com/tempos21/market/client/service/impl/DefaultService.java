package com.tempos21.market.client.service.impl;

import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.Service;
import com.tempos21.market.client.service.impl.DefaultServiceHandler.OnServiceResponseListener;

import java.util.HashMap;

public abstract class DefaultService implements Service, OnServiceResponseListener {

    private String url = "";
    private HashMap<String, String> parameters;
    private HashMap<String, String> entityParameters;
    private HashMap<String, String> restParameters;

    public DefaultService(String url) {
        this.url = url;
        parameters = new HashMap<String, String>();
        entityParameters = new HashMap<String, String>();
        restParameters = new HashMap<String, String>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public HashMap<String, String> getParameters() {
        return parameters;
    }

    @Override
    public HashMap<String, String> getEntityParameters() {
        return entityParameters;
    }

    public HashMap<String, String> getRestParameters() {
        return restParameters;
    }

    public void setRestParameters(HashMap<String, String> restParameters) {
        this.restParameters = restParameters;
    }

    public void clearParameters() {
        parameters.clear();
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public void clearEntitiyParameters() {
        entityParameters.clear();
    }

    public void addEntityParameter(String key, String value) {
        entityParameters.put(key, value);

    }

    public void addRestParameter(String key, String value) {
        restParameters.put(key, value);
    }

    @Override
    public abstract void onServiceResponse(int responseCode, ServiceResponse response);


}
