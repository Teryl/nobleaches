package com.example.nobleaches;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton back_button = findViewById(R.id.backButton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Login.this, Authentication.class);
                startActivity(intent);
            }
        });

        EditText ID_Input = findViewById(R.id.Student_ID);
        EditText Password_Input = findViewById(R.id.Password_Input);
        Button innerLogIn_Button = findViewById(R.id.innerLogInButton);
        UserList userList = UserListSingleton.getInstance();
        List<UserData> users = userList.getUserList();
        innerLogIn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredId = ID_Input.getText().toString();
                String enteredPassword = Password_Input.getText().toString();
                UserData authenticatedUser = null;

                // Search for the authenticated user in the user list
                for (UserData user : users) {
                    if (user.getUserId().equals(enteredId) && user.getUserPassword().equals(enteredPassword)) {
                        // If found, assign the user to authenticatedUser
                        authenticatedUser = user;
                        break;
                    }
                }

                if (authenticatedUser != null) {
                    // Save the authenticated user data to UserDataManager
                    UserDataManager.getInstance(getApplicationContext()).saveUserDataToDataManager(authenticatedUser);
                    SharedPreferences.Editor editor = getSharedPreferences("login_pref", MODE_PRIVATE).edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();
                    // Start the Home activity
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.putExtra("source", "Login");
                    startActivity(intent);
                } else {
                    // Display a toast message for invalid credentials
                    Toast.makeText(Login.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
