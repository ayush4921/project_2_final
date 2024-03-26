package com.example.project2final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Allows users to track their mood, anxiety level, and
 * medication adherence, and save this information to a database.
 */
public class Tracker extends AppCompatActivity {

    private RadioGroup radioGroupMood;
    private SeekBar seekBarAnxiety;
    private CheckBox checkBoxMedication;
    private TextView textViewMedicationAdherence;
    private RadioGroup radioGroupMedicationAdherence;
    private Button buttonSubmit;

    private DBClass db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        radioGroupMood = findViewById(R.id.radioGroupMood);
        seekBarAnxiety = findViewById(R.id.seekBarAnxiety);
        checkBoxMedication = findViewById(R.id.checkBoxMedication);
        textViewMedicationAdherence = findViewById(R.id.textViewMedicationAdherence);
        radioGroupMedicationAdherence = findViewById(R.id.radioGroupMedicationAdherence);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Get the user ID from the intent
        userId = getIntent().getIntExtra("USERID", -1);

        // Initialize the database
        db = new DBClass(this);

        checkBoxMedication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewMedicationAdherence.setVisibility(View.VISIBLE);
                    radioGroupMedicationAdherence.setVisibility(View.VISIBLE);
                } else {
                    textViewMedicationAdherence.setVisibility(View.GONE);
                    radioGroupMedicationAdherence.setVisibility(View.GONE);
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected mood
                int selectedMoodId = radioGroupMood.getCheckedRadioButtonId();
                String mood = "";
                if (selectedMoodId == R.id.radioButtonNeutral) {
                    mood = "Neutral";
                } else if (selectedMoodId == R.id.radioButtonSad) {
                    mood = "Sad";
                } else if (selectedMoodId == R.id.radioButtonVerySad) {
                    mood = "Very Sad";
                } else if (selectedMoodId == R.id.radioButtonHappy) {
                    mood = "Happy";
                } else if (selectedMoodId == R.id.radioButtonVeryHappy) {
                    mood = "Very Happy";
                }

                // Get the anxiety level
                int anxietyLevel = seekBarAnxiety.getProgress();

                // Get the medication adherence
                String medicationAdherence = "";
                if (checkBoxMedication.isChecked()) {
                    int selectedMedicationAdherenceId = radioGroupMedicationAdherence.getCheckedRadioButtonId();
                    if (selectedMedicationAdherenceId == R.id.radioButtonYes) {
                        medicationAdherence = "Yes";
                    } else if (selectedMedicationAdherenceId == R.id.radioButtonNo) {
                        medicationAdherence = "No";
                    } else if (selectedMedicationAdherenceId == R.id.radioButtonNotApplicable) {
                        medicationAdherence = "Not Applicable";
                    }
                }

                // Save the tracker entry to the database
                db.addTrackerEntry(mood, anxietyLevel, medicationAdherence, userId);

                // Show a toast message
                Toast.makeText(Tracker.this, "Tracker entry saved", Toast.LENGTH_SHORT).show();

                // Finish the activity
                finish();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}