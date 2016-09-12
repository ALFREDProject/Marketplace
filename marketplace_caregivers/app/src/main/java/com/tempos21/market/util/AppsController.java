package com.tempos21.market.util;

import android.content.Context;

import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.db.AppModel;
import com.tempos21.market.ui.fragment.HomeFragment;


/**
 * This class controls if the apps of myApps section are really installed in our phone
 *
 * @author A519130
 */
public class AppsController {

    /**
     * This method revises if all apps of myApps are installed in device and update apps in server
     *
     * @param context             MyAppsFragment context
     * @param installedServerApps apps installed in server
     * @return all alfredo market apps installed in device
     */
    public static Apps reviseApps(Context context, Apps installedServerApps) {
        AppModel model = new AppModel(context);
        // Actualizamos la BD por si se han instalado nuevas aplicaciones mediante otras fuentes ajenas al AlfredoMarket
        model.clearAppsTable();
        model.setApps(HomeFragment.installedApps);

        Apps allMarketInstalledApps = model.getApps();
        Apps insertOnServerApps = model.getApps();

        // Comprobamos si las apps instaladas en el servidor estan en nuestro terminal
        for (App app : installedServerApps) {
            if (!model.isOnDB(app)) {
                AppsStateControl update = new AppsStateControl(context);
                update.getInstalledApps();
            } else {
                insertOnServerApps.remove(app);
            }
        }

        // Actualizamos el servidor con las apps que hemos instalado directamente en el terminal sin usar el Alfredo Market
        for (App app : insertOnServerApps) {
            AppsStateControl update = new AppsStateControl(context, app);
            update.getInstalledApps();
        }

        return allMarketInstalledApps;
    }
}
