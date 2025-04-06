package com.example.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {
    private PDFView pdfView;
    private ImageView backButton;
    private ImageView homeButton;
    private ImageView infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Получаем только верхний отступ (для статус-бара)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets; // Возвращаем insets без изменений
        });


        pdfView = findViewById(R.id.pdfView);
        backButton = findViewById(R.id.backButton);
        homeButton = findViewById(R.id.imageView3);
        infoButton = findViewById(R.id.infoButton);

        String pdfPath = getIntent().getStringExtra("pdf_path");

        if (pdfPath != null) {
            openPdf(pdfPath);
        } else {
            Toast.makeText(this, "PDF не найден", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(v -> finish());

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(PdfViewerActivity.this, MainActivity.class);
            startActivity(intent);
        });

        infoButton.setOnClickListener(v -> {
            Intent intent = new Intent(PdfViewerActivity.this, InfoActivity.class);
            startActivity(intent);
        });
    }

    private void openPdf(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                // Открытие из файловой системы
                Log.d("PdfViewerActivity", "Открываем файл из файловой системы: " + filePath);
                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load();
            } else {
                Log.d("PdfViewerActivity", "Файл не найден в системе, пробуем открыть из assets: " + filePath);
                pdfView.fromAsset(filePath)  // Открытие из assets
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при открытии PDF", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
