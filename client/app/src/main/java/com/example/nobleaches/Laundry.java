package com.example.nobleaches;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.System;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Laundry extends AppCompatActivity{
    private MachineAdapter adapter;
    private RecyclerView mRecyclerView;

    private Handler mHandler = new Handler();
    private Runnable refresherRunnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);

        // Starting Updater Service
        startService(new Intent(this, MachineUpdateService.class));


        mRecyclerView = findViewById(R.id.recyclerViewLaundryMachines);
        adapter = new MachineAdapter();
        mRecyclerView.setAdapter(adapter);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton back_button = findViewById(R.id.backButton);

        refresherRunnable = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                mHandler.postDelayed(this, 500);
            }
        };

        mHandler.post(refresherRunnable);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Laundry.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
