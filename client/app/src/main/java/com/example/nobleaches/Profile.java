package com.example.nobleaches;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Profile extends AppCompatActivity {
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        userData = UserDataManager.getInstance(getApplicationContext()).getUserData();
        TextView Name = findViewById(R.id.Name);
        Name.setText(userData.getUserName());
        ImageButton back_button = findViewById(R.id.backButton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this, Home.class);
                startActivity(intent);
            }
        });



        LinearLayout My_Profile_Button = findViewById(R.id.My_Profile);
        My_Profile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity
                Intent intent = new Intent(Profile.this, My_Profile.class);
                startActivity(intent);
            }
        });

        LinearLayout History_Button = findViewById(R.id.History);
        History_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity
                Intent intent = new Intent(Profile.this, History.class);
                startActivity(intent);
            }
        });

        LinearLayout Settings_Button = findViewById(R.id.Settings);
        Settings_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity
                Intent intent = new Intent(Profile.this, Settings.class);
                startActivity(intent);
            }
        });

        Button Log_Out_Button = findViewById(R.id.Log_Out);
        Log_Out_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity
                Intent intent = new Intent(Profile.this, Authentication.class);
                startActivity(intent);
            }
        });

    }
}

