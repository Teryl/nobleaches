package com.example.nobleaches;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Home extends AppCompatActivity {
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();


        if (intent != null) {
            String source = intent.getStringExtra("source");
            if (source != null && (source.equals("Authentication") ||
                    source.equals("Register") ||
                    source.equals("Login"))) {
                Toast.makeText(Home.this, "Welcome!", Toast.LENGTH_SHORT).show();
            }
        }

        Button button = findViewById(R.id.LAUNDRY);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity
                Intent intent = new Intent(Home.this, Laundry.class);
                startActivity(intent);
            }
        });


        userData = UserDataManager.getInstance(getApplicationContext()).getUserData();
        Button Profile_Button = findViewById(R.id.PROFILE);
        Profile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData.getUserId().equals("Guest")){
                    Intent intent = new Intent(Home.this, Register.class);
                    startActivity(intent);
                }
                else {
                    // Start the new activity
                    Intent intent = new Intent(Home.this, Profile.class);
                    startActivity(intent);
                }
            }
        });
    }
}
