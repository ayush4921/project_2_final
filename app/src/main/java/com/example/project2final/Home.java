package com.example.project2final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        DBClass db = new DBClass(this);


        // Retrieve the username passed from the MainActivity
        String username = intent.getStringExtra("USERNAME");
        int userId = intent.getIntExtra("USERID",-1);
        TextView usernameTextView = findViewById(R.id.textViewName);
        usernameTextView.setText("Hello, " + username);
        Button dailyDiary = findViewById(R.id.btnDailyDiary);
        Button myNotes = findViewById(R.id.btnMyNotes);
        Button tracker = findViewById(R.id.btnTracker);
        Button links = findViewById(R.id.btnHelpfulLinks);
        Button logOut = findViewById(R.id.buttonLogOut);

        dailyDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDailyDiary = new Intent(Home.this, DailyDiary.class);
                intentDailyDiary.putExtra("USERNAME", username);
                intentDailyDiary.putExtra("USERID", userId);
                startActivity(intentDailyDiary);
            }
        });


        myNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyNotes = new Intent(Home.this, MyNotes.class);
                intentMyNotes.putExtra("USERID", userId);
                startActivity(intentMyNotes);
            }
        });

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTracker = new Intent(Home.this, Tracker.class);
                startActivity(intentTracker);
            }
        });

        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLinks = new Intent(Home.this, HelpfulLinks.class);
                startActivity(intentLinks);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogOut = new Intent(Home.this, MainActivity.class);
                startActivity(intentLogOut);
            }
        });


    }
}