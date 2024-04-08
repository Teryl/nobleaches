package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
}
