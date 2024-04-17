package com.example.nobleaches;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GlobalConfig {
    private Properties config;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference machineStatus;
    DatabaseReference bookingRequestRef;
    FirebaseFirestore firestoreDb;
    List<HistoryRecord> historyRecordList;
    private static final int UPDATE_INTERVAL = 500; // Update interval in ms
    private static GlobalConfig instance;
    Gson gson = new Gson();

    private GlobalConfig() {
        config = new Properties();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        firestoreDb = FirebaseFirestore.getInstance();
        historyRecordList = new ArrayList<>();
        machineStatus = database.getInstance("https://nobleaches-80ab2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Machines");
        bookingRequestRef = database.getInstance("https://nobleaches-80ab2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("BookingRequests");
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
