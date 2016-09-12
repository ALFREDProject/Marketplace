package com.tempos21.market.client.bean;

import org.codehaus.jackson.JsonNode;

/***
 * This bean will contain the basic response from any service. Data field will contain
 * the unparsed Json value for the specific answer.
 *
 * @author Sergi Martinez
 */
public class ServiceResponse {
    private String error_code;
    private JsonNode data;
    private String status;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public int getIntError_code() {
        return Integer.parseInt(error_code);
    }

    public JsonNode getData() {
        if (data == null) {
            return null;
        }
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
