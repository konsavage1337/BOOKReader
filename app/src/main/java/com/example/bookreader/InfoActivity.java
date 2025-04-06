package com.example.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoActivity extends AppCompatActivity {

    private ImageView homeButton;
    private ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);

        homeButton = findViewById(R.id.homeButton);
        addButton = findViewById(R.id.addButton);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this, MainActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(InfoActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }
}