package com.example.nobleaches;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nobleaches.MachineUpdateService;

import java.util.List;

public class Laundry extends AppCompatActivity implements MachineUpdateListener {
    private MachineAdapter adapter;
    private RecyclerView mRecyclerView;
    private MachineUpdateService machineUpdateService;
    private boolean isBound = false;
    private Handler mHandler = new Handler();
    private Runnable refresherRunnable;
    private View dimView;
    private View filter_overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        // Starting Updater Service
        Intent updater = new Intent(this, MachineUpdateService.class);
        startService(updater);

        // Bind to MachineUpdateService
        bindService(updater, serviceConnection, Context.BIND_AUTO_CREATE);

        mRecyclerView = findViewById(R.id.recyclerViewLaundryMachines);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton back_button = findViewById(R.id.backButton);

        mHandler.post(refresherRunnable);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        Button Filter_Button = findViewById(R.id.filter_button);
        dimView = findViewById(R.id.dimView_filter);
        filter_overlay = findViewById(R.id.filter_overlay);

        Filter_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimView.setVisibility(View.VISIBLE);
                filter_overlay.setVisibility(View.VISIBLE);


                ImageButton Close_Button = findViewById(R.id.filter_close_button);
                Button Find_Filter_Button = findViewById(R.id.Find_Filter_Button);

                Close_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dimView.setVisibility(View.GONE);
                        filter_overlay.setVisibility(View.GONE);
                    }
                });

                Find_Filter_Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dimView.setVisibility(View.GONE);
                        filter_overlay.setVisibility(View.GONE);
                        //Trigger MachineAdapter sort
                    }
                });

            }
        });
    }

    // Define ServiceConnection to get reference to MachineUpdateService
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MachineUpdateService.LocalBinder binder = (MachineUpdateService.LocalBinder) service;
            machineUpdateService = binder.getService();
            isBound = true;

            // Once connected to the service, initialize the adapter with the machineList
            if (machineUpdateService != null) {
                adapter = new MachineAdapter(Laundry.this,machineUpdateService.getMachineList());
                machineUpdateService.setListener(Laundry.this);
                mRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            machineUpdateService = null;
            adapter = null;
        }
    };

    public void onMachineDataUpdate(List<MachineData> machineList) {
        // Update UI with the new machine data
        adapter.updateData(machineList); // Assuming you have a method in your adapter to update the data
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}
