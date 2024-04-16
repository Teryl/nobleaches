package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class History extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ImageButton back_button = findViewById(R.id.backButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(History.this, Profile.class);
                startActivity(intent);
            }
        });

        // Find RecyclerView
        recyclerView = findViewById(R.id.recyclerViewUserHistory);

        // Create and set HistoryAdapter
        historyAdapter = new HistoryAdapter(this);
        recyclerView.setAdapter(historyAdapter);

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
