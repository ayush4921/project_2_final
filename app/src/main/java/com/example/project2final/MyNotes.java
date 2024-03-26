package com.example.project2final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyNotes extends AppCompatActivity {

    private ListView listViewNotes;
    private Button backButton;
    private Button deleteButton;
    private EditText editTextFilter;
    private DBClass db;
    private int userId;
    private List<String> notes;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        listViewNotes = findViewById(R.id.listViewNotes);
        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);
        editTextFilter = findViewById(R.id.editTextFilter);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USERID", -1);

        db = new DBClass(this);

        // Retrieve the user's notes from the database
        notes = db.getAllNotes(userId);

        // Create an ArrayAdapter to populate the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, notes);
        listViewNotes.setAdapter(adapter);
        listViewNotes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedNotes();
            }
        });

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void deleteSelectedNotes() {
        List<String> selectedNotes = new ArrayList<>();
        for (int i = 0; i < listViewNotes.getCount(); i++) {
            if (listViewNotes.isItemChecked(i)) {
                selectedNotes.add(notes.get(i));
            }
        }
        for (String note : selectedNotes) {
            db.deleteNote(note, userId);
            notes.remove(note);
        }
        adapter.notifyDataSetChanged();
    }

    private void filterNotes(String keyword) {
        List<String> filteredNotes = new ArrayList<>();
        for (String note : notes) {
            if (note.toLowerCase().contains(keyword.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        adapter.clear();
        adapter.addAll(filteredNotes);
        adapter.notifyDataSetChanged();
    }
}