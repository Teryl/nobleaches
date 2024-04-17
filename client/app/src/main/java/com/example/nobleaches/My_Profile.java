package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class My_Profile extends AppCompatActivity {
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        userData = UserDataManager.getInstance(getApplicationContext()).getUserData();
        TextView Name = findViewById(R.id.Name_Profile);
        TextView Email = findViewById(R.id.Email_Profile);

        Name.setText(userData.getUserName());
        Email.setText(userData.getUserEmail());

        ImageButton back_button = findViewById(R.id.backButton);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(My_Profile.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    public static class MainActivity extends AppCompatActivity {
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

            GlobalConfig.getInstance().setConfig(config);

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
}
