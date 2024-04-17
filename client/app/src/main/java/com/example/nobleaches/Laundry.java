package com.example.nobleaches;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class Laundry extends AppCompatActivity implements MachineUpdateListener {
    private MachineAdapter adapter;
    private RecyclerView mRecyclerView;
    private MachineUpdateService machineUpdateService;
    private boolean isBound = false;
    private Handler mHandler = new Handler();
    Intent updater;
    private Runnable refresherRunnable;
    private View dimView;
    private View filter_overlay;
    private List<MachineData> mainMachineList = new ArrayList<>();
    private List<MachineData> displayMachineList = new ArrayList<>();
    boolean block55Selected = false;
    boolean block57Selected = false;
    boolean block59Selected = false;
    boolean washerSelected = false;
    boolean dryerSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        // Starting Updater Service
        updater = new Intent(this, MachineUpdateService.class);
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
                finish();
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

                Chip block55Chip= findViewById(R.id.chip_block_55);
                Chip block57Chip= findViewById(R.id.chip_block_57);
                Chip block59Chip= findViewById(R.id.chip_block_59);
                Chip washerChip= findViewById(R.id.chip_washer);
                Chip dryerChip= findViewById(R.id.chip_dryer);


                ImageButton Close_Button = findViewById(R.id.filter_close_button);
                Button Find_Filter_Button = findViewById(R.id.Find_Filter_Button);

                block55Chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        block55Selected = isChecked;
                        Log.d("Laundry.java", "55 set to "+block55Selected);
                    }
                });

                block57Chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        block57Selected = isChecked;
                        Log.d("Laundry.java", "57 set to "+block57Selected);
                    }
                });

                block59Chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        block59Selected = isChecked;
                        Log.d("Laundry.java", "59 set to "+block59Selected);
                    }
                });

                washerChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        washerSelected = isChecked;
                        Log.d("Laundry.java", "Washer set to "+washerSelected);
                    }
                });

                dryerChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dryerSelected = isChecked;
                        Log.d("Laundry.java", "Dryer set to "+dryerSelected);
                    }
                });

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
                        onMachineDataUpdate(mainMachineList);
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
        mainMachineList.clear();
        displayMachineList.clear();
        mainMachineList.addAll(machineList);
        for (MachineData machine : mainMachineList) {
            // Check if the machine matches the filter criteria
            if ((!washerSelected || machine.getName().toLowerCase().contains("washer")) &&
                    (!dryerSelected || machine.getName().toLowerCase().contains("dryer")) &&
                    (!block55Selected || machine.getBlock().equals("55")) &&
                    (!block57Selected || machine.getBlock().equals("57")) &&
                    (!block59Selected || machine.getBlock().equals("59"))) {
                // Add the machine to the filtered list
                displayMachineList.add(machine);
            }
        }

        adapter.updateData(displayMachineList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        stopService(updater);
        Log.d("Laundry.java", "Killed Updater");
    }
}
