package com.example.project2final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * Sets up buttons to open specific web pages related to mental health
 * resources when clicked.
 */

public class HelpfulLinks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpful_links);

        Button lifeLine = findViewById(R.id.button988);

        lifeLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://988lifeline.org");
            }
        });

        Button findTreatment = findViewById(R.id.buttonFindTreatment);

        findTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://findtreatment.gov");
            }
        });

        Button caringForMentalHealth = findViewById(R.id.buttonCaringForMentalHealth);

        caringForMentalHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://www.nimh.nih.gov/health/topics/caring-for-your-mental-health");
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

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}