package com.example.bookreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 58;
    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_DATE = "publication_date";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PDF_PATH = "pdf_path";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_IS_USER_UPLOADED = "is_user_uploaded";  // Новый столбец для категории

    private static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID_NAME = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    // Создание таблицы для книг
    private static final String CREATE_BOOKS_TABLE =
            "CREATE TABLE " + TABLE_BOOKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_AUTHOR + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_PDF_PATH + " TEXT, " +
                    COLUMN_CATEGORY_ID + " INTEGER, " +
                    COLUMN_IS_USER_UPLOADED + " INTEGER, " +// Связь с категорией
                    "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID_NAME + "));";

    // Создание таблицы для категорий
    private static final String CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIES_TABLE); // Создание таблицы категорий
        db.execSQL(CREATE_BOOKS_TABLE); // Создание таблицы книг
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_BOOKS + " ADD COLUMN " + COLUMN_CATEGORY_ID + " INTEGER;");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    private void seedData(SQLiteDatabase db) {
        // Вставка данных в таблицу категорий
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (category_name) VALUES " +
                "('IT')," +
                "('Сказки')," +
                "('Наука')," +
                "('Ужасы');");

        // Вставка данных в таблицу книг с ID категорий
        db.execSQL("INSERT INTO " + TABLE_BOOKS + " (title, author, publication_date, image, pdf_path, category_id) VALUES " +
                "('Мир в ореховой скорлупе', 'Стивен Хокинг', '2001', 'png1', 'mir_v_skorlupe.pdf', 3)," +
                "('Thinking Java', 'Bruce Eckel', '1995', 'png2', 'ThinkingInJava.pdf', 1)," +
                "('Занимательная астрономия', 'Я.И. Перельман', '1922', 'png3', 'astronom.pdf', 3)," +
                "('Последнее обращение к человечеству', 'Левашов Николай', '1997', 'png4', 'obrash.pdf', 3)," +
                "('C# Programming for Absolute Beginners', 'Radek Vystavel', '2008', 'png5', 'program1.pdf', 1)," +
                "('Книга джунглей', 'Джозеф Киплинг', '1894', 'png6', 'kniigadz.pdf', 2)," +
                "('Сказка о царе Салтане', 'А.С. Пушкин', '1950', 'png7', 'pushkin.pdf', 2);");
    }

    // Загрузка книг из базы данных с учетом категории

    public Cursor getUserUploadedBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOKS, null, COLUMN_IS_USER_UPLOADED + " = 1", null, null, null, null);
    }
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOKS, null, null, null, null, null, null);
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES, null, null, null, null, null, null);
    }

    public void addPdfItem(PdfItem pdfItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, pdfItem.getTitle());
        values.put(COLUMN_PDF_PATH, pdfItem.getPdfPath());
        values.put(COLUMN_IS_USER_UPLOADED, 1);  // Отметка, что это пользовательский файл

        db.insert(TABLE_BOOKS, null, values);
        db.close();
    }

    public void deletePdfItem(int pdfId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, COLUMN_ID + " = ?", new String[]{String.valueOf(pdfId)});
        db.close();
    }
}