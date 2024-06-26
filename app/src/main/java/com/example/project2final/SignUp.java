package com.example.project2final;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles user sign up functionality including form validation and database
 * interaction.
 */
public class SignUp extends AppCompatActivity {

    private EditText nameEditText, ageEditText, usernameEditText, passwordEditText;
    private Spinner genderSpinner;
    private Button submitButton;
    private DBClass databaseHelper;
    private String selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();

        DBClass db = new DBClass(this);

        List<String> options = new ArrayList<>();
        options.add("Select Gender"); // This is the hint
        options.add("Male");
        options.add("Female");
        options.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) { // Hint's position
                    textView.setText(getItem(position));
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setSelection(0); // Set the hint as the default selection

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGender = null;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String gender = selectedGender;
                String name = nameEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());

                // Username validation
                if (username.length() < 5) {
                    Toast.makeText(SignUp.this, "Username must be at least 5 characters long.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!username.matches("^[a-z0-9]+$")) {
                    Toast.makeText(SignUp.this, "Username must be alphanumeric and all lowercase with no spaces.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Password validation
                if (password.length() < 8) {
                    Toast.makeText(SignUp.this, "Password must be at least 8 characters long.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Character.isUpperCase(password.charAt(0))) {
                    Toast.makeText(SignUp.this, "Password must start with an uppercase letter.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.matches(".*[0-9].*")) {
                    Toast.makeText(SignUp.this, "Password must contain at least one number.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.contains(" ")) {
                    Toast.makeText(SignUp.this, "Password cannot contain spaces.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Gender validation
                if (selectedGender == null || selectedGender.equals("Select Gender")) {
                    Toast.makeText(SignUp.this, "Please select a gender.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Check if the user already exists
                boolean userAdded = db.addUser(name, age, gender, username, password);

                if (userAdded) {
                    // User added successfully, redirect to the login screen
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User already exists, display an alert
                    Toast.makeText(SignUp.this, "Username already exists. Please choose a different username.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.editTextName);
        genderSpinner = findViewById(R.id.genderSpinner);
        ageEditText = findViewById(R.id.editTextAge);
        usernameEditText = findViewById(R.id.editTextUsernameSignup);
        passwordEditText = findViewById(R.id.editTextPasswordSignup);
        submitButton = findViewById(R.id.buttonSubmitSignup);
    }
}