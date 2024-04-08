package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText Name_Input = findViewById(R.id.Name_Register);
        EditText Student_ID_Input = findViewById(R.id.Student_ID_Register);
        EditText Email_Input = findViewById(R.id.Email_Register);
        EditText Password_Input = findViewById(R.id.Password_Register);

        ImageButton back_button = findViewById(R.id.backButton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Register.this, Authentication.class);
                startActivity(intent);
            }
        });

        Button registerButton = findViewById(R.id.innerRegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input data
                String name = Name_Input.getText().toString();
                String studentId = Student_ID_Input.getText().toString();
                String email = Email_Input.getText().toString();
                String password = Password_Input.getText().toString();

                // Create a new UserData object
                UserData newUser = new UserData(studentId, name, password, email);

                // Add the new user to the UserList or UserListSingleton
                UserListSingleton.getInstance().addUser(newUser);
                UserDataManager.getInstance(getApplicationContext()).saveUserDataToDataManager(newUser);
                // Start the authentication activity or any other activity
                Intent intent = new Intent(Register.this, Home.class);
                intent.putExtra("source", "Register");
                startActivity(intent);
            }
        });
    }
}
