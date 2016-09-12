package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.harvest.AsyncJsonHarvester;
import com.tempos21.market.client.harvest.AsyncJsonHarvester.OnHarvestFinishedListener;
import com.tempos21.market.client.harvest.HarvestPetition;
import com.tempos21.market.client.harvest.HarvestResult;
import com.tempos21.market.client.harvest.JsonHarvester;
import com.tempos21.market.client.http.T21HttpClientWithSSL;
import com.tempos21.market.client.service.Service;
import com.tempos21.market.client.service.ServiceHandler;

import org.apache.http.ParseException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class DefaultServiceHandler implements OnHarvestFinishedListener, ServiceHandler {
    private Service service;
    private HarvestPetition petition;
    private HarvestResult result;
    private OnServiceResponseListener onServiceResponseListener;
//	private Context context;

    public DefaultServiceHandler(Context context, Service service) {
//		this.context=context;
        this.service = service;
    }

    ;

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#getService()
     */
    @Override
    public Service getService() {
        return service;
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#setService(com.tempos21.Alfredomarket.client.service.Service)
     */
    @Override
    public void setService(Service service) {
        this.service = service;
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#runGetService()
     */
    @Override
    public HarvestResult runGetService() throws ParseException, IOException {
        buildHarvestPetition();
        petition.setParamsMap(service.getParameters());
        petition.setEntityMap(service.getEntityParameters());
        petition.setRestMap(service.getRestParameters());
        JsonHarvester harvester = new JsonHarvester(petition);
        result = harvester.executeGetRequest();
        return result;

    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#runPostService()
     */
    @Override
    public HarvestResult runPostService() throws ParseException, IOException {
        buildHarvestPetition();
        petition.setEntityMap(service.getEntityParameters());
        JsonHarvester harvester = new JsonHarvester(petition);
        result = harvester.executeGetRequest();
        return result;

    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#getResultType()
     */
    @Override
    public ResultType getResultType() throws JsonParseException, IOException {
        if (result == null || result.getJsonAnswer() == null)
            return null;
        else {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jp = jsonFactory.createJsonParser(result.getJsonAnswer());
            if (jp.nextToken() == JsonToken.START_ARRAY)
                return ResultType.ARRAY;
            else
                return ResultType.OBJECT;

        }

    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#runGetServiceAsync()
     */
    @Override
    public void runGetServiceAsync() {
        runServiceAsync(HarvestPetition.PetitionType.GET);
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#runPostServiceAsync()
     */
    @Override
    public void runPostServiceAsync() {
        runServiceAsync(HarvestPetition.PetitionType.POST);
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#runServiceAsync(com.tempos21.Alfredomarket.client.harvest.HarvestPetition.PetitionType)
     */
    @Override
    public void runServiceAsync(HarvestPetition.PetitionType type) {
        buildHarvestPetition();
        petition.setEntityMap(service.getEntityParameters());
        petition.setParamsMap(service.getParameters());
        petition.setRestMap(service.getRestParameters());
        petition.setPreferred(type);
        AsyncJsonHarvester harvester = new AsyncJsonHarvester();
        harvester.setOnHarvestFinishedListener(this);
        harvester.execute(petition);

    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#getEntity(java.lang.Class)
     */
    @Override
    public <T> T getEntity(Class<T> valueType) throws JsonParseException,
            JsonMappingException, IOException {
        T item = null;
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            item = mapper.readValue(result.getJsonAnswer(), valueType);
        }
        return item;
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#getOnServiceResponseListener()
     */
    @Override
    public OnServiceResponseListener getOnServiceResponseListener() {
        return onServiceResponseListener;
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#setOnServiceResponseListener(com.tempos21.Alfredomarket.client.service.DefaultServiceHandler.OnServiceResponseListener)
     */
    @Override
    public void setOnServiceResponseListener(
            OnServiceResponseListener onServiceResponseListener) {
        this.onServiceResponseListener = onServiceResponseListener;
    }

    private void buildHarvestPetition() {
        petition = new HarvestPetition(service.getUrl(), T21HttpClientWithSSL.getInstance());
        petition.addHeader("Content-Type", "application/x-www-form-urlencoded");
        petition.addHeader("Accept", "application/json");
        petition.addHeader("Accept-Charset", "utf-8");
    }

    /* (non-Javadoc)
     * @see com.tempos21.Alfredomarket.client.service.ServiceHandler#onHarvestFinished(com.tempos21.Alfredomarket.client.harvest.HarvestResult)
     */
    @Override
    public void onHarvestFinished(HarvestResult harvestResult) {
        int responseCode = ServiceErrorCodes.OK;
        result = harvestResult;
        ServiceResponse response = null;
        if (result.getHttpStatus() == ServiceErrorCodes.HTTP_OK) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                response = mapper.readValue(result.getJsonAnswer(), ServiceResponse.class);

            } catch (JsonParseException e) {
                responseCode = ServiceErrorCodes.PARSER_ERROR;
            } catch (JsonMappingException e) {
                responseCode = ServiceErrorCodes.MAPPER_ERROR;
            } catch (IOException e) {
                responseCode = ServiceErrorCodes.IO_ERROR;
            }
        } else {
            if (result.getHttpStatus() == ServiceErrorCodes.HTTP_403) {
                responseCode = ServiceErrorCodes.HTTP_403;
            } else {
                if (result.getHttpStatus() == ServiceErrorCodes.LOGIN_ERROR) {
                    responseCode = ServiceErrorCodes.LOGIN_ERROR;
                } else {
                    responseCode = ServiceErrorCodes.WRONG_RESPONSE;
                }
            }
        }
        if (onServiceResponseListener != null) {
            onServiceResponseListener.onServiceResponse(responseCode, response);
        }
    }

    public enum ResultType {
        ARRAY, OBJECT
    }

    public interface OnServiceResponseListener {
        public void onServiceResponse(int responseCode, ServiceResponse response);

    }
}
