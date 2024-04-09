package com.example.nobleaches;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

public class GlobalConfig {
    private Properties config;
    FirebaseDatabase database;
    DatabaseReference machineStatus;
    private static final int UPDATE_INTERVAL = 500; // Update interval in ms
    private static GlobalConfig instance;

    private GlobalConfig() {
        config = new Properties();
        database = FirebaseDatabase.getInstance();
        machineStatus = database.getInstance("https://nobleaches-80ab2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Machines");
    }

    public static synchronized GlobalConfig getInstance() {
        if (instance == null) {
            instance = new GlobalConfig();
        }
        return instance;
    }

    public int getUpdateInterval() {
        return UPDATE_INTERVAL;
    }

    public DatabaseReference getMachineFullList(){
        return machineStatus;
    }
    public Properties getConfig() {
        return config;
    }

    public void setConfig(final Properties config) {
        this.config = config;
    }
}
