package com.tempos21.market.client.harvest;

import com.tempos21.market.client.http.T21HttpClientWithSSL;


public class HarvestResult {

    public static final int REASON_IOERROR = 1;
    public static final int REASON_PARSEERROR = 2;

    private String jsonResult = null;
    private int httpStatus;
    private int id = 0;
    private T21HttpClientWithSSL httpClient;
    private int reason;


    public HarvestResult(String jsonResult) {

        this.jsonResult = jsonResult;

    }

    public String getJsonAnswer() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int statusCode) {
        this.httpStatus = statusCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T21HttpClientWithSSL getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(T21HttpClientWithSSL httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String toString() {
        return "HarvestResult [ jsonResult=" + jsonResult + ", httpStatus=" + httpStatus + ", id=" + id + "]";
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }


}
