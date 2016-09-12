package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.input.Input;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetUpdatableAppsService extends DefaultSecureService {

    OnGetAppsServiceResponseListener onGetAppsServiceResponseListener;
    DefaultServiceHandler serviceHandler;
    private Apps apps = new Apps();
    private ObjectMapper mapper;


    public GetUpdatableAppsService(String url, Context context) {
        super(url, ServiceType.GET, context);
    }

    public OnGetAppsServiceResponseListener getOnServiceResponseListener() {
        return onGetAppsServiceResponseListener;
    }

    public void setOnGetAppsServiceResponseListener(OnGetAppsServiceResponseListener onServiceResponseListener) {
        this.onGetAppsServiceResponseListener = onServiceResponseListener;
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
                ErrorCode = saveApps(elements, responseCode);
            } else {
                apps = null;
                ErrorCode = Integer.parseInt(response.getError_code());
            }
        }
        if (onGetAppsServiceResponseListener != null) {
            onGetAppsServiceResponseListener.onGetAppsServiceResponse(ErrorCode, apps);
        }
    }


    private int saveApps(JsonNode node, int errorCode) {
        try {
            if (node.has("app")) {
                JsonNode elements = node.get("app");

                apps = mapper.readValue(elements, Apps.class);
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


    public interface OnGetAppsServiceResponseListener {
        public void onGetAppsServiceResponse(int responseCode, Apps apps);
    }

}
