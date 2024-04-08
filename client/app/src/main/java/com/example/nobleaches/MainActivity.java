package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to start the Home activity
                Intent intent = new Intent(MainActivity.this, Authentication.class);
                startActivity(intent);

                // Finish the current activity to prevent going back to it with back button
                finish();
            }
        }, 3000);
    }

}
