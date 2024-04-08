package com.example.nobleaches;

import android.content.res.AssetManager;
import android.content.Context;
import android.content.res.Resources;
import android.security.ConfirmationCallback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {
    private Properties config;
    private static ConfigReader instance;

    private ConfigReader() {
        config = new Properties();
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(final Properties config) {
        this.config = config;
    }
}
