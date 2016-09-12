package com.tempos21.market.client.harvest;

import android.os.AsyncTask;

/**
 * This class is responsible for executing a JsonHarvester object in
 * asyncrhonous mode and return the HarvestResult to the listener
 */

/**
 * @author A511218
 */
public class AsyncJsonHarvester extends AsyncTask<HarvestPetition, Void, HarvestResult> {
    private OnHarvestFinishedListener onHarvestFinishedListener;


    /**
     * Executes the background call to JsonHarvester.  Only the first
     * HarvestPetition will be served, no queue. Should we do it queueable?
     *
     * @param params The HarvestPetition. It should be responsible for selecting
     *               a GET or POST method
     * @see android.os.AsyncTask#doInBackground(Params[])
     */

    @Override
    protected HarvestResult doInBackground(HarvestPetition... params) {
        HarvestResult harvestResult = null;
        if (params.length > 0) {
            HarvestPetition petition = params[0];
            JsonHarvester harvester = new JsonHarvester(petition);
            harvestResult = harvester.executePreferredRequest();


        }
        return harvestResult;
    }

    /**
     * Executed when the async task is finished. Basically it warns the
     * onHarvestFinishedListener if exists
     *
     * @param harvestResult The result of the harvesting
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    protected void onPostExecute(HarvestResult harvestResult) {
        if (onHarvestFinishedListener != null)
            onHarvestFinishedListener.onHarvestFinished(harvestResult);
    }

    /**
     * Sets the responsible to be warned when the harvest finished
     *
     * @param onHarvestFinishedListener
     */
    public void setOnHarvestFinishedListener(OnHarvestFinishedListener onHarvestFinishedListener) {
        this.onHarvestFinishedListener = onHarvestFinishedListener;
    }

    /**
     * This interface is used to represent an object that will be warned when
     * the petitions is finished.
     *
     * @author Sergi Martinez
     */
    public interface OnHarvestFinishedListener {
        /**
         * Called when the harvest is finished
         *
         * @param harvestResult
         */
        void onHarvestFinished(HarvestResult harvestResult);

    }
}
