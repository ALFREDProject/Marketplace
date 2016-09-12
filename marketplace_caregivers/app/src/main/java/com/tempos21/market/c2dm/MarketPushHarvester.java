package com.tempos21.market.c2dm;

import android.content.Context;

import com.tempos21.market.client.harvest.AsyncJsonHarvester;
import com.tempos21.market.client.harvest.AsyncJsonHarvester.OnHarvestFinishedListener;
import com.tempos21.market.client.harvest.HarvestPetition;
import com.tempos21.market.client.harvest.HarvestPetition.PetitionType;
import com.tempos21.market.client.harvest.HarvestResult;
import com.tempos21.market.client.http.T21HttpClientWithSSL;
import com.tempos21.market.util.TLog;

import org.apache.http.protocol.HTTP;

/**
 * Al utilizar la libreria t21c2dm-lib y seguir el model de la aplicacion de
 * TravelClub, se utiliza esta clase practicamente copiada para el registro (y
 * desregistro) en el servidor de Tempos21
 *
 * @author
 */
public class MarketPushHarvester implements OnHarvestFinishedListener {

    private static final String URL_PUSH = "/device/settoken";

    private static final String REGISTER_SERVICE_URL = com.tempos21.market.Constants.BASE_URL
            + URL_PUSH;

    public void pushRegister(Context context, String registrationId) {
        TLog.d("pushRegister");
        String fullUrl = REGISTER_SERVICE_URL;

        AsyncJsonHarvester asyncHarvester = new AsyncJsonHarvester();
        HarvestPetition petition = new HarvestPetition(fullUrl, T21HttpClientWithSSL.getInstance());
        petition.addHeader("Accept", "application/json");
        petition.addHeader("Accept-Encoding", HTTP.UTF_8);
        petition.addEntity("token", registrationId);
        petition.setPreferred(PetitionType.POST);
        asyncHarvester.setOnHarvestFinishedListener(this);
        TLog.i("REGISTERING PUSH " + petition.getUrl());

        asyncHarvester.execute(petition);
    }

    /**
     * Se ejecuta cuando se termina la peticion
     *
     * @param harverstResult
     */
    @Override
    public void onHarvestFinished(HarvestResult harvestResult) {
        TLog.i("PUSH REGISTERED ");
    }
}
