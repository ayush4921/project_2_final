package com.example.project2final;
import android.content.Intent;
import android.health.connect.datatypes.AppInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signUp = findViewById(R.id.buttonSignUp);
        Button logIn = findViewById(R.id.buttonLogin);
        EditText usernameText = findViewById(R.id.editTextUsername);
        EditText passwordText = findViewById(R.id.editTextPassword);
        DBClass db = new DBClass(this, "Users");

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
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.putExtra("USERNAME_KEY", enteredUsername);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_LONG).show();
                    passwordText.setText("");
                }
            }
        });

    }
}
