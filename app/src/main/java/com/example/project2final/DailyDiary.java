package com.example.project2final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DailyDiary extends AppCompatActivity {

    private EditText diaryEntryText;
    private Button saveButton;
    private Button backButton;
    private DBClass db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary);

        diaryEntryText = findViewById(R.id.multilineEditTextDiary);
        saveButton = findViewById(R.id.buttonSave);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        db = new DBClass(this);
        userId = intent.getIntExtra("USERID",-1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diaryEntry = diaryEntryText.getText().toString();
                if (!diaryEntry.isEmpty()) {
                    db.addNote(diaryEntry, userId);
                    Toast.makeText(DailyDiary.this, "Diary entry saved", Toast.LENGTH_SHORT).show();
                    diaryEntryText.setText("");
                } else {
                    Toast.makeText(DailyDiary.this, "Please enter a diary entry", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}