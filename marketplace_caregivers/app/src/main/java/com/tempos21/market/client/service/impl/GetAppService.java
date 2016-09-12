package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.input.Input;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetAppService extends DefaultSecureService {

    OnGetAppServiceResponseListener onGetAppServiceResponseListener;
    DefaultServiceHandler serviceHandler;
    private App app = new App();
    private ObjectMapper mapper;


    public GetAppService(String url, Context context) {
        super(url, ServiceType.GET, context);
    }

    public OnGetAppServiceResponseListener getOnServiceResponseListener() {
        return onGetAppServiceResponseListener;
    }

    public void setOnGetAppServiceResponseListener(OnGetAppServiceResponseListener onServiceResponseListener) {
        this.onGetAppServiceResponseListener = onServiceResponseListener;
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
                JsonNode data = response.getData();
                ErrorCode = saveApps(data, responseCode);
            } else {
                app = null;
                ErrorCode = Integer.parseInt(response.getError_code());
            }
        }
        if (onGetAppServiceResponseListener != null) {
            onGetAppServiceResponseListener.onGetAppServiceResponse(ErrorCode, app);
        }
    }

    public void setupParameters(String id) {
        clearParameters();
        addRestParameter("id", id);
    }

    public void setupParameterVersion(String version) {
        addRestParameter("version", version);
    }

    private int saveApps(JsonNode node, int errorCode) {

        try {

            app = mapper.readValue(node, App.class);

        } catch (JsonParseException e) {
            errorCode = ServiceErrorCodes.PARSER_ERROR;
        } catch (JsonMappingException e) {
            errorCode = ServiceErrorCodes.MAPPER_ERROR;
        } catch (IOException e) {
            errorCode = ServiceErrorCodes.IO_ERROR;
        }

        return errorCode;
    }


    public interface OnGetAppServiceResponseListener {
        public void onGetAppServiceResponse(int responseCode, App app);
    }

}
