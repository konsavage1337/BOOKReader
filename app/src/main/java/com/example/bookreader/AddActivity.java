package com.example.bookreader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private static final int PICK_PDF_FILE = 1;
    private Uri pdfUri;
    private Button btnSelectPdf;
    private ListView pdfListView;
    private ArrayList<PdfItem> pdfList;
    private PdfAdapter pdfAdapter;
    private DatabaseHelper databaseHelper;
    private ImageView homeButton;
    private ImageView infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Инициализация
        btnSelectPdf = findViewById(R.id.btnSelectPdf);
        pdfListView = findViewById(R.id.pdfListView);
        databaseHelper = new DatabaseHelper(this);

        homeButton = findViewById(R.id.homeButton);
        infoButton = findViewById(R.id.infoButton);

        // Создание списка PDF
        pdfList = new ArrayList<>();
        pdfAdapter = new PdfAdapter(this, pdfList);
        pdfListView.setAdapter(pdfAdapter);

        // Загрузка только пользовательских PDF
        loadUserUploadedPdfs();

        // Обработчик кнопки для выбора PDF
        btnSelectPdf.setOnClickListener(view -> selectPdfFile());

        // Обработчик нажатия на элемент списка
        pdfListView.setOnItemClickListener((parent, view, position, id) -> {
            PdfItem selectedPdf = pdfList.get(position);
            openPdfViewer(selectedPdf);
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        });

        infoButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, InfoActivity.class);
            startActivity(intent);
        });
    }

    // Загрузка PDF, загруженных пользователем
    private void loadUserUploadedPdfs() {
        Cursor cursor = databaseHelper.getUserUploadedBooks();
        pdfList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)); // Получаем ID
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                String pdfPath = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PDF_PATH));
                pdfList.add(new PdfItem(id, title, pdfPath)); // Используем ID из базы данных
            } while (cursor.moveToNext());
            cursor.close();
        }
        pdfAdapter.notifyDataSetChanged();
    }

    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    private void savePdf(String title) {
        if (pdfUri != null && !title.isEmpty()) {
            String pdfPath = FileUtils.getFilePathFromUri(this, pdfUri); // Конвертация Uri в путь к файлу
            if (pdfPath != null) {
                PdfItem newPdf = new PdfItem(0, title, pdfPath); // ID = 0, если это новый PDF
                databaseHelper.addPdfItem(newPdf); // Сохраняем PDF в базе данных
                pdfList.add(newPdf);
                pdfAdapter.notifyDataSetChanged();
                Toast.makeText(this, "PDF добавлен: " + title, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка при сохранении PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Выберите PDF и введите название", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                pdfUri = data.getData();
                showTitleDialog();  // Показать диалог для ввода названия
            }
        }
    }

    private void showTitleDialog() {
        // Создаем диалог для ввода названия
        final EditText input = new EditText(this);
        input.setHint("Введите название книги");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите название")
                .setView(input)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String title = input.getText().toString().trim();
                    savePdf(title);  // Сохраняем PDF с введенным названием
                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void openPdfViewer(PdfItem selectedPdf) {
        Intent intent = new Intent(this, PdfViewerActivity.class);
        intent.putExtra("pdf_path", selectedPdf.getPdfPath());  // Передаем правильный путь
        intent.putExtra("pdfTitle", selectedPdf.getTitle());
        startActivity(intent);
    }
}
