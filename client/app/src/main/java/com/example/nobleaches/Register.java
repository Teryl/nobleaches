package com.example.nobleaches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        EditText Name_Input = findViewById(R.id.Name_Register);
        EditText Email_Input = findViewById(R.id.Email_Register);
        EditText Password_Input = findViewById(R.id.Password_Register);

        ImageButton back_button = findViewById(R.id.backButton);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String email = Email_Input.getText().toString();
                String password = Password_Input.getText().toString();

                // Start the authentication activity or any other activity

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = Objects.requireNonNull(task.getResult().getUser());
                                    firebaseUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
                                    Toast.makeText(Register.this, "Account Created",
                                            Toast.LENGTH_SHORT).show();

                                    // Create a new UserData object
                                    UserDataManager.getInstance(getApplicationContext()).saveFirebaseUserWithDispName(firebaseUser, name);

                                    Intent intent = new Intent(Register.this, Home.class);
                                    intent.putExtra("source", "Register");
                                    startActivity(intent);

                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Account creation failed. " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
