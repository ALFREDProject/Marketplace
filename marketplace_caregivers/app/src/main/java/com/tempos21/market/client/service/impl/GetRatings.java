package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.Ratings;
import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.input.Input;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetRatings extends DefaultSecureService {

    OnGetRatingsServiceResponseListener onGetRatingsServiceResponseListener;
    DefaultServiceHandler serviceHandler;
    private Ratings Ratings = new Ratings();
    private ObjectMapper mapper;


    public GetRatings(String url, Context context) {
        super(url, ServiceType.GET, context);
    }

    public OnGetRatingsServiceResponseListener getOnServiceResponseListener() {
        return onGetRatingsServiceResponseListener;
    }

    public void setOnGetRatingsServiceResponseListener(OnGetRatingsServiceResponseListener onServiceResponseListener) {
        this.onGetRatingsServiceResponseListener = onServiceResponseListener;
    }

    public DefaultServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public void setServiceHandler(DefaultServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void runService(Input input) {
        setupParameters(input);
        super.runService(input);
    }

    @Override
    public void onSecureServiceResponse(int responseCode, ServiceResponse response) {
        int ErrorCode = responseCode;

        if (responseCode == ServiceErrorCodes.HTTP_OK) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            if (response.getData() != null) {
                JsonNode elements = response.getData();
                ErrorCode = saveRatings(elements, responseCode);
            } else {
                Ratings = null;
                ErrorCode = Integer.parseInt(response.getError_code());
            }
        }
        if (onGetRatingsServiceResponseListener != null) {
            onGetRatingsServiceResponseListener.onGetRatingsServiceResponse(ErrorCode, Ratings);
        }
    }

    public void setupParameters(String id) {
        clearParameters();
        addRestParameter("id", id);
    }

    private int saveRatings(JsonNode node, int errorCode) {
        try {
            if (node.has("appRateResponse")) {
                JsonNode elements = node.get("appRateResponse");

                Ratings = mapper.readValue(elements, Ratings.class);
            }
        } catch (JsonParseException e) {
            errorCode = ServiceErrorCodes.PARSER_ERROR;
        } catch (JsonMappingException e) {
            errorCode = ServiceErrorCodes.MAPPER_ERROR;
        } catch (IOException e) {
            errorCode = ServiceErrorCodes.IO_ERROR;
        }

        return errorCode;
    }


    public interface OnGetRatingsServiceResponseListener {
        public void onGetRatingsServiceResponse(int responseCode, Ratings ratings);
    }

}
