package com.example.project2final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private DBClass db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUp = findViewById(R.id.buttonSignUp);
        Button logIn = findViewById(R.id.buttonLogin);
        usernameText = findViewById(R.id.editTextUsername);
        passwordText = findViewById(R.id.editTextPassword);
        db = new DBClass(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = usernameText.getText().toString();
                String enteredPassword = passwordText.getText().toString();

                if (db.isValidLogin(enteredUsername, enteredPassword)) {
                    String userName = db.getUserName(enteredUsername);
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.putExtra("USERNAME", userName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_LONG).show();
                    passwordText.setText("");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable the back button
        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();
    }
}