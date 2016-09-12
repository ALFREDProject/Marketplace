package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.input.GetAppsInput;
import com.tempos21.market.client.service.input.Input;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetInstalledAppsService extends DefaultSecureService {

    OnGetInstalledAppsServiceResponseListener onGetInstalledAppsServiceResponseListener;
    DefaultServiceHandler serviceHandler;
    private Apps apps = new Apps();
    private ObjectMapper mapper;


    public GetInstalledAppsService(String url, Context context) {
        super(url, ServiceType.GET, context);
    }

    public OnGetInstalledAppsServiceResponseListener getOnServiceResponseListener() {
        return onGetInstalledAppsServiceResponseListener;
    }

    public void setOnGetInstalledAppsServiceResponseListener(OnGetInstalledAppsServiceResponseListener onServiceResponseListener) {
        this.onGetInstalledAppsServiceResponseListener = onServiceResponseListener;
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
    public void setupParameters(Input input) {
        clearEntitiyParameters();
        addEntityParameter(Fields.SORTING, ((GetAppsInput) input).getSorting());
        addEntityParameter(Fields.START, Integer.toString(((GetAppsInput) input).getStart()));
        addEntityParameter(Fields.ELEMENTS, Integer.toString(((GetAppsInput) input).getElements()));
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
        if (onGetInstalledAppsServiceResponseListener != null) {
            onGetInstalledAppsServiceResponseListener.onGetInstalledAppsServiceResponse(ErrorCode, apps);
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


    public interface OnGetInstalledAppsServiceResponseListener {
        public void onGetInstalledAppsServiceResponse(int responseCode, Apps apps);
    }

    private class Fields {
        public static final String SORTING = "sorting";
        public static final String START = "start";
        public static final String ELEMENTS = "elements";
    }

}
