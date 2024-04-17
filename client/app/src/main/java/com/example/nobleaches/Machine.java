package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class Machine extends AppCompatActivity {
    private View dimView;
    private View overlayReserve;
    private View overlayStart;
    private SeekBar timerSeekBar;
    private TextView selectedDurationTextView;
    private final Handler handler = new Handler();
    private MachineData machineData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        MachineListReader machinelist = MachineListReader.getInstance(getApplicationContext());

        Intent intent = getIntent();
        String machineName = intent.getStringExtra("machine_Type");
        String machineStatus = intent.getStringExtra("machine_Status");
        String machineBlock = intent.getStringExtra("machine_Block");
        String machineLastUsed = intent.getStringExtra("machine_LastUsed");

        machineData = machinelist.getMachineDataByType(machineName);

        TextView Type = findViewById(R.id.Type);
        TextView Availability = findViewById(R.id.availabilityTextView);
        TextView Block = findViewById(R.id.Block);
        TextView LastUsed = findViewById(R.id.LastUsed);

        Type.setText(machineName + "-" + machineBlock);
        Availability.setText("Availability: " + machineStatus);
        Block.setText("Block " + machineBlock);
        LastUsed.setText("Last Used: " + machineLastUsed);

        ImageButton back_button = findViewById(R.id.backButton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Machine.this, Laundry.class);
                startActivity(intent);
            }
        });

        Button reserve_button = findViewById(R.id.reserve);
        dimView = findViewById(R.id.dimView);
        overlayReserve = findViewById(R.id.overlayreserve);


        reserve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!machineStatus.toString().equals("Available") && !machineStatus.toString().equals("In Use")){
                    Toast.makeText(Machine.this, "Machine cannot be booked", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    BookingRequest bookingRequest = new BookingRequest(machineData);
                    String json = GlobalConfig.getInstance().gson.toJson(bookingRequest);
                    GlobalConfig.getInstance().bookingRequestRef.child(bookingRequest.getBookingUUID()).setValue(json);
                }
                LastUsed.setVisibility(View.GONE);


                TextView availabilityTextView = findViewById(R.id.availabilityTextView);
                availabilityTextView.setText(getString(R.string.Reserved));
                dimView.setVisibility(View.VISIBLE);
                overlayReserve.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        machineData.setStatus("Available");
                        availabilityTextView.setText("Available");

                        dimView.setVisibility(View.GONE);
                        overlayReserve.setVisibility(View.GONE);
                    }
                }, 8000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dimView.setVisibility(View.GONE);
                        overlayReserve.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        });

        Button start_button = findViewById(R.id.start);
        overlayStart = findViewById(R.id.overlaystart);
        timerSeekBar = findViewById(R.id.timerSeekBar);
        View startOverlay = findViewById(R.id.overlaystart);
        selectedDurationTextView = startOverlay.findViewById(R.id.selectedDurationTextView);

        timerSeekBar.setMax(90); // Represents the range from 30 to 120 minutes
        timerSeekBar.setProgress(0); // Initial progress represents 30 minutes

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Availability.getText().toString().equals("Available")){
                    Toast.makeText(Machine.this, "Machine is not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                View overlayStartSuccess = findViewById(R.id.overlaystartsuccess);
                dimView.setVisibility(View.VISIBLE);
                overlayStart.setVisibility(View.VISIBLE);
                timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    int selectedDuration = timerSeekBar.getProgress() + 30;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        selectedDuration = progress + 30; // Calculate the selected duration
                        selectedDurationTextView.setText(selectedDuration + " minutes");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                Button inner_start_button = startOverlay.findViewById(R.id.start);
                inner_start_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedDuration = timerSeekBar.getProgress() + 30;
                        String machineStatus = selectedDuration + " mins left";

                        // Set the machine status
                        machinelist.getMachineDataByType(machineName).setStatus(machineStatus);

                        // Update the availability TextView
                        TextView availabilityTextView = findViewById(R.id.availabilityTextView);
                        availabilityTextView.setText(timerSeekBar.getProgress() + 30 + " minutes left");
                        dimView.setVisibility(View.VISIBLE);
                        overlayStartSuccess.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dimView.setVisibility(View.GONE);
                                overlayStart.setVisibility(View.GONE);
                                overlayStartSuccess.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }
                });
            }
        });
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending delayed actions to prevent leaks
        handler.removeCallbacksAndMessages(null);

    }
}
