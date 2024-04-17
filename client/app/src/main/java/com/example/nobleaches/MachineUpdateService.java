package com.example.nobleaches;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MachineUpdateService extends Service {
    private List<MachineData> machineList;
    private List<MachineData> filteredMachineList;
    private Handler handler;
    private final IBinder binder = new LocalBinder();
    private MachineUpdateListener refreshListener;

    // Get Realtime Firebase Instance
    DatabaseReference dbRef = GlobalConfig.getInstance().getMachineFullList();

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        machineList = new ArrayList<>();
        Log.d("Machine Updater Service", "Started");

        // Listen for changes in Firebase Realtime Database
        dbRef.addValueEventListener(machineDataListener);

        handler.postDelayed(periodicUpdateTask, 500);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // No need to fetch data here, it's handled by ValueEventListener
//        handler.postDelayed(updateStatusTask, UPDATE_INTERVAL);
        return START_STICKY;
    }

    public List<MachineData> getMachineList() {
        return machineList;
    }

    public class LocalBinder extends Binder {
        MachineUpdateService getService() {
            return MachineUpdateService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setListener(MachineUpdateListener listener) {
        this.refreshListener = listener;
    }

//    private void updateAdapter(List<MachineData> newData) {
//        if (refreshListener != null) {
//            refreshListener.onMachineDataUpdate(newData);
//        }
//    }

    private ValueEventListener machineDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //Log
            Log.d("MachineUpdateService", "Data changed in Firebase");
            // Clear the existing machine list
            machineList.clear();
            // Iterate through each machine's data
            for (DataSnapshot machineSnapshot : dataSnapshot.getChildren()) {
                // Extract machine details from the snapshot
                String name = machineSnapshot.child("name").getValue(String.class);
                String status = machineSnapshot.child("status").getValue(String.class);
                String block = machineSnapshot.child("block").getValue(String.class);
                String lastUsed = machineSnapshot.child("lastUsed").getValue(String.class);
                String machineTopic = machineSnapshot.child("machineTopic").getValue(String.class);
                // Create a new MachineData object and add it to the list
                MachineData machineData = new MachineData(name, status, block, lastUsed, machineTopic);
                machineList.add(machineData);
            }
            // Notify the listener
            if (refreshListener != null) {
                refreshListener.onMachineDataUpdate(machineList);
                Log.d("MachineUpdateService", "Machine List changed by service");
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Handle onCancelled event
            Log.e("MachineUpdateService", "Database error: " + databaseError.getMessage());
        }
    };

    private Runnable periodicUpdateTask = new Runnable() {
        @Override
        public void run() {
            // Update the listener with the most recent machine list every 5 seconds
            if (refreshListener != null) {
                refreshListener.onMachineDataUpdate(machineList);
                Log.d("MachineUpdateService", "Periodic listener update");
            }
            // Schedule the next update after 5 seconds
            handler.postDelayed(this, GlobalConfig.getInstance().getUpdateInterval());
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove ValueEventListener to avoid memory leaks
        dbRef.removeEventListener(machineDataListener);
        // Remove updates when service is destroyed
        Log.d("Machine Updater Service", "Stopped");
    }
}
