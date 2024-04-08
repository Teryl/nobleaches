package com.example.nobleaches;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Authentication extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        UserDataManager userDataManager = UserDataManager.getInstance(getApplicationContext());

        if (userDataManager.isLoggedIn() && mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Authentication.this, Home.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_authentication);
        }

        Button LogIn_Button = findViewById(R.id.LogInButton);
        LogIn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Authentication.this, Login.class);
                startActivity(intent);
            }
        });

        Button Register_Button = findViewById(R.id.RegisterButton);
        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Authentication.this, Register.class);
                startActivity(intent);
            }
        });

        Button Guest_Button = findViewById(R.id.GuestButton);
        Guest_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                UserData Guest = new UserData("Guest");
                UserDataManager.getInstance(getApplicationContext()).saveUserDataToDataManager(Guest);
                Intent intent = new Intent(Authentication.this, Home.class);
                intent.putExtra("source", "Authentication");
                startActivity(intent);
            }
        });
    }
}
