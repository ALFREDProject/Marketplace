package com.tempos21.market.client.service;

import com.tempos21.market.client.harvest.HarvestPetition;
import com.tempos21.market.client.harvest.HarvestResult;
import com.tempos21.market.client.service.impl.DefaultServiceHandler.OnServiceResponseListener;
import com.tempos21.market.client.service.impl.DefaultServiceHandler.ResultType;

import org.apache.http.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

public interface ServiceHandler {

    public abstract Service getService();

    public abstract void setService(Service service);

    public abstract HarvestResult runGetService() throws ParseException,
            IOException;

    public abstract HarvestResult runPostService() throws ParseException,
            IOException;

    public abstract ResultType getResultType() throws JsonParseException,
            IOException;

    public abstract void runGetServiceAsync();

    public abstract void runPostServiceAsync();

    public abstract void runServiceAsync(HarvestPetition.PetitionType type);

    public abstract <T> T getEntity(Class<T> valueType)
            throws JsonParseException, JsonMappingException, IOException;

    public abstract void onHarvestFinished(HarvestResult harvestResult);

    public abstract OnServiceResponseListener getOnServiceResponseListener();

    public abstract void setOnServiceResponseListener(
            OnServiceResponseListener onServiceResponseListener);

}