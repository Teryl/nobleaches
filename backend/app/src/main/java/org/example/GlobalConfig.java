package org.example;

import java.io.*;
import java.nio.*;
import java.util.Properties;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.cloud.firestore.*;

import com.google.gson.Gson;

public class GlobalConfig {
    private static GlobalConfig instance;
    private Properties config = new Properties();
    Gson gson = new Gson();

    public static final String TOPIC1 = "block-55";
    public static final String TOPIC2 = "block-59";
    public static final String TOPIC3 = "block-57";
    public static final String UUID = "Hp9rUAG4kXO5NvrCjqBBHPkKqaq2";
    public static final String DATABASE_URL = "https://nobleaches-80ab2-default-rtdb.asia-southeast1.firebasedatabase.app";
    public static final String FIRESTORE_COLLECTION = "booking-history";
    public static final String FIRESTORE_DOCUMENT = "booking-history";
    public static final String KAFKA_TOPIC = "booking-requests";

    final FirebaseApp firebaseApp = FirebaseApp.getInstance();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference ref = database.getReference("Machines");
    final DatabaseReference bookingRef = database.getReference("BookingRequests");
    
    private GlobalConfig() {
    }

    public static synchronized GlobalConfig getInstance() {
        if (instance == null) {
            synchronized (GlobalConfig.class) {
                if (instance == null) {
                    instance = new GlobalConfig();
                }
            }
        }
        return instance;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}