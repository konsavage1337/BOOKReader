package com.example.bookreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView categoryRecycler;
    CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseHelper databaseHelper;
    private List<Category> categoryList;
    private ImageView infoButton;
    private ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoButton = findViewById(R.id.infoButton);
        addButton = findViewById(R.id.addButton);

        // Настройка RecyclerView для отображения книг
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        // Загружаем все книги по умолчанию
        bookList = loadBooksFromDatabase();
        bookAdapter = new BookAdapter(this, bookList, book -> {
            Intent intent = new Intent(MainActivity.this, PdfViewerActivity.class);
            intent.putExtra("pdf_path", book.getPdfPath());
            startActivity(intent);
        });

        recyclerView.setAdapter(bookAdapter);

        // Список категорий
        categoryList = new ArrayList<>();
        categoryList.add(new Category(0, "Все"));  // Общая категория "Все книги"
        categoryList.add(new Category(1, "IT"));
        categoryList.add(new Category(2, "Сказки"));
        categoryList.add(new Category(3, "Наука"));
        categoryList.add(new Category(4, "Ужасы"));

        // Настройка RecyclerView для отображения категорий
        setCategoryRecycler(categoryList);

        infoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }

    // Метод для отображения категорий
    private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList, category -> {
            if (category.getId() == 0) {
                // Если выбрана категория "Все книги", показываем все книги
                bookList.clear();
                bookList.addAll(loadBooksFromDatabase());
            } else {
                // Фильтруем книги по выбранной категории
                bookList.clear();
                bookList.addAll(loadBooksByCategory(category.getId()));
            }
            bookAdapter.notifyDataSetChanged();  // Обновляем список книг
        });

        categoryRecycler.setAdapter(categoryAdapter);
    }

    // Метод для загрузки всех книг без фильтрации
    private List<Book> loadBooksFromDatabase() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));
                String publicationDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
                String pdfPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PDF_PATH));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID));
                books.add(new Book(id, title, author, publicationDate, image, pdfPath, categoryId));
            }
            cursor.close();
        }
        db.close();
        return books;
    }

    // Метод для загрузки книг по категории
    private List<Book> loadBooksByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(categoryId) };
        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKS, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AUTHOR));
                String publicationDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
                String pdfPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PDF_PATH));
                books.add(new Book(id, title, author, publicationDate, image, pdfPath, categoryId));
            }
            cursor.close();
        }
        db.close();
        return books;
    }
}
