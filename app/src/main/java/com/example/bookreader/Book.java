package com.example.bookreader;

public class Book {
    private int id;
    private int categoryId;
    private String title;
    private String author;
    private String date;
    private String image;
    private String pdfPath;


    // Конструктор с 6 параметрами
    public Book(int id, String title, String author, String date, String image, String pdfPath, int categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.image = image;
        this.pdfPath = pdfPath;
        this.categoryId = categoryId;
    }

    // Геттеры
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getPdfPath() {
        return pdfPath;
    }
}
