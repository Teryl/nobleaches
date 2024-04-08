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

import java.util.Objects;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton back_button = findViewById(R.id.backButton);
        mAuth = FirebaseAuth.getInstance();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Authentication.class);
                startActivity(intent);
            }
        });

        EditText ID_Input = findViewById(R.id.Email_Input);
        EditText Password_Input = findViewById(R.id.Password_Input);
        Button innerLogIn_Button = findViewById(R.id.innerLogInButton);
        innerLogIn_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredId = ID_Input.getText().toString();
                String enteredPassword = Password_Input.getText().toString();

                mAuth.signInWithEmailAndPassword(enteredId, enteredPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login Successful!",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser firebaseUser = task.getResult().getUser();
                                    UserDataManager.getInstance(getApplicationContext()).saveFirebaseUser(Objects.requireNonNull(firebaseUser));

                                    Intent intent = new Intent(Login.this, Home.class);
                                    intent.putExtra("source", "Login");
                                    startActivity(intent);

                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}
