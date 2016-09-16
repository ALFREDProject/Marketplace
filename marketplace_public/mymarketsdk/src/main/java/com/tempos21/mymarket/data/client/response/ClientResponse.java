package com.tempos21.mymarket.data.client.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientResponse<D> {

    @SerializedName("error_code")
    @Expose
    public Integer errorCode;
    @Expose
    public String status;
    @Expose
    public D data;
}
