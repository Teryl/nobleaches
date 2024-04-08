package com.example.nobleaches;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.renderscript.ScriptGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Initializing Kafka Config
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("client.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Loading config onto Singleton Class
        Properties config = new Properties();
        try {
            config.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ConfigReader.getInstance().setConfig(config);

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
