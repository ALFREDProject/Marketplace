package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.ReportsUpdates;
import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.input.Input;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class TransactionsService extends DefaultSecureService {

    private static final String CHANGE = "changes";
    OnModifyInstalledAppsServiceResponseListener onModifyInstalledAppsServiceResponseListener;
    DefaultServiceHandler serviceHandler;
    private ReportsUpdates reports;
    private ObjectMapper mapper;


    public TransactionsService(String url, Context context) {
        super(url, ServiceType.POST, context);
    }

    public OnModifyInstalledAppsServiceResponseListener getOnServiceResponseListener() {
        return onModifyInstalledAppsServiceResponseListener;
    }

    public void setOnModifyInstalledAppsServiceResponseListener(OnModifyInstalledAppsServiceResponseListener onServiceResponseListener) {
        this.onModifyInstalledAppsServiceResponseListener = onServiceResponseListener;
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

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String aux = mapper.writeValueAsString(input);
            addEntityParameter(CHANGE, aux.replace("transactions", "change"));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSecureServiceResponse(int responseCode, ServiceResponse response) {
        int ErrorCode = responseCode;

        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (response != null && response.getData() != null) {
            JsonNode elements = response.getData();
            ErrorCode = saveReports(elements, responseCode);
        } else {
            reports = null;
            if (response == null) {
                ErrorCode = ServiceErrorCodes.WRONG_RESPONSE;
            } else {

                ErrorCode = Integer.parseInt(response.getError_code());
            }
        }

        if (onModifyInstalledAppsServiceResponseListener != null) {
            onModifyInstalledAppsServiceResponseListener.onModifyInstalledAppsServiceResponse(ErrorCode, reports);
        }
    }


    private int saveReports(JsonNode node, int errorCode) {
        try {
            if (node.getElements().hasNext())
                reports = mapper.readValue(node, ReportsUpdates.class);

        } catch (JsonParseException e) {
            errorCode = ServiceErrorCodes.PARSER_ERROR;
        } catch (JsonMappingException e) {
            errorCode = ServiceErrorCodes.MAPPER_ERROR;
        } catch (IOException e) {
            errorCode = ServiceErrorCodes.IO_ERROR;
        }

        return errorCode;
    }


    public interface OnModifyInstalledAppsServiceResponseListener {
        public void onModifyInstalledAppsServiceResponse(int responseCode, ReportsUpdates reports);
    }

}
