package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.Countries;
import com.tempos21.market.client.bean.ServiceResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GetCountriesService extends DefaultSecureService {

    DefaultServiceHandler serviceHandler;
    private Countries countries = new Countries();
    private OnGetCountriesServiceResponseListener onGetCountriesServiceResponseListener;
    private ObjectMapper mapper;


    public GetCountriesService(String url, Context context) {
        super(url, ServiceType.GET, context);
    }


    @Override
    public void runService() {
        super.runService();
    }


    @Override
    public void onSecureServiceResponse(int responseCode, ServiceResponse response) {
        int ErrorCode = responseCode;

        if (responseCode == ServiceErrorCodes.HTTP_OK) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            if (response.getData() != null) {
                JsonNode elements = response.getData();
                ErrorCode = saveCountries(elements, responseCode);
            } else {
                countries = null;
                ErrorCode = Integer.parseInt(response.getError_code());
            }
        }
        if (onGetCountriesServiceResponseListener != null) {
            onGetCountriesServiceResponseListener.onGetCountriesResponse(ErrorCode, countries);
        }
    }


    private int saveCountries(JsonNode node, int errorCode) {
        try {
            if (node.has("countryResponse")) {
                JsonNode elements = node.get("countryResponse");
                countries = mapper.readValue(elements, Countries.class);
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


    public OnGetCountriesServiceResponseListener getOnServiceResponseListener() {
        return onGetCountriesServiceResponseListener;
    }

    public void setOnGetCountriesServiceResponseListener(OnGetCountriesServiceResponseListener onServiceResponseListener) {
        this.onGetCountriesServiceResponseListener = onServiceResponseListener;
    }

    public DefaultServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public void setServiceHandler(DefaultServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }


    public interface OnGetCountriesServiceResponseListener {
        public void onGetCountriesResponse(int responseCode, Countries countries);
    }
}
