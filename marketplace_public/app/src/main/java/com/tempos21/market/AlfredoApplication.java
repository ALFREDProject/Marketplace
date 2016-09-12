package com.tempos21.market;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.MyMarketConfig;
import com.tempos21.mymarket.sdk.model.Platform;

import java.util.UUID;

/**
 * Created by a519130 on 18/06/2015.
 */
public class AlfredoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        configMarketSDK();
        configUniversalLoader();
    }


    private void configMarketSDK(){
        MyMarketConfig config = new MyMarketConfig.Builder(this).build(); // MyMarket configuration builder
        MyMarket.init(this, config);
    }


    private void configUniversalLoader(){
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

}
