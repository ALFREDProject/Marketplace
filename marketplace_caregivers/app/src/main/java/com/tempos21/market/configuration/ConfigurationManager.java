package com.tempos21.market.configuration;

import com.tempos21.market.util.TLog;

public class ConfigurationManager {
    private static Configuration configuration = null;

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        ConfigurationManager.configuration = configuration;
        TLog.i("Configuration set for Base URL " + configuration.getBaseUrl());
    }
}
