package com.example.nobleaches;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class MachineUpdateService extends Service {
    private List<MachineData> machineList;
    private Handler handler;
    private Random random;
    private static final int UPDATE_INTERVAL = 500; // Update interval in ms

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        machineList = new ArrayList<>();
        random = new Random();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        machineList = MachineListReader.getInstance().getMachineList();
        handler.postDelayed(updateStatusTask, UPDATE_INTERVAL);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable updateStatusTask = new Runnable() {
        @Override
        public void run() {
            //Code to update each machine
            for (MachineData machine : machineList) {
                //Update Status
                String status = getRandomStatus();
                machine.setStatus(status);

                //Broadcast changes
                Intent intent = new Intent("machine_status_updated");
                intent.putExtra("machine_type", machine.getType());
                intent.putExtra("machine_status", status);
                sendBroadcast(intent);
            }

            //Schedule Update
            handler.postDelayed(this, UPDATE_INTERVAL);
        }
    };

    private String getRandomStatus() {
        char randomLetter = (char) (random.nextInt(5) + 'A');
        return "Status: " + randomLetter;
    }

    public void onDestroy() {
        super.onDestroy();
        //Remove updates when service is destroyed
        handler.removeCallbacks(updateStatusTask);
    }
}
