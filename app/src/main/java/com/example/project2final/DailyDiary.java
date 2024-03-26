package com.example.project2final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DailyDiary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        DBClass db = new DBClass(this);

        int userId = db.getUserId(username);

        EditText diaryEntryText = findViewById(R.id.multilineEditTextDiary);
        String diaryEntry = diaryEntryText.getText().toString();
        db.addNote(diaryEntry, userId);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}