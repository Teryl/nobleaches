package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.platforminfo.GlobalLibraryVersionRegistrar;

import org.apache.kafka.common.protocol.types.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    List<HistoryRecord> historyRecordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ImageButton back_button = findViewById(R.id.backButton);

        historyRecordList = new ArrayList<>();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(History.this, Profile.class);
                startActivity(intent);
            }
        });

        // Find RecyclerView
        recyclerView = findViewById(R.id.recyclerViewUserHistory);

        //Call firestore objects
        getAllDocumentsFromCollection(historyRecordList);
    }

    public void getAllDocumentsFromCollection(List<HistoryRecord> historyRecordList) {
        historyRecordList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("booking-history")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String bookingUUID = document.getString("bookingUUID");
                                String userUUID = document.getString("userUUID");
                                Date requestTime = document.getDate("requestTime");
                                Date processedOn = document.getDate("processedOn");


                                Map<String, Object> machineDataMap = (Map<String, Object>) document.get("machine");
                                String machineName = (String) machineDataMap.get("name");
                                String machineStatus = (String) machineDataMap.get("status");
                                String machineTopic = (String) machineDataMap.get("machineTopic");
                                String machineBlock = (String) machineDataMap.get("block");
                                String machineLastUsed = (String) machineDataMap.get("lastUsed");
                                MachineData machineData = new MachineData(machineName, machineStatus, machineBlock, machineLastUsed, machineTopic);

                                // Creating new HistoryRecord object
                                HistoryRecord historyRecord = new HistoryRecord();
                                //Populating entries
                                historyRecord.setBookingUUID(bookingUUID);
                                historyRecord.setUserUUID(userUUID);
                                historyRecord.setMachine(machineData);
                                historyRecord.setRequestTime(requestTime);
                                historyRecord.setProcessedOn(processedOn);

                                historyRecordList.add(historyRecord);
                            }
                            // Create and set HistoryAdapter after fetching data
                            historyAdapter = new HistoryAdapter(History.this, History.this.historyRecordList);
                            recyclerView.setAdapter(historyAdapter);
                            // Set LayoutManager
                            recyclerView.setLayoutManager(new LinearLayoutManager(History.this));
                            Log.d("History", ""+historyRecordList.size());
                        } else {
                            // Handle errors
                            Log.d("ConfigGlobel", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
