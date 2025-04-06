package com.example.bookreader;
public class PdfItem {
    private int id; // ID PDF
    private String title;
    private String pdfPath;

    // Конструктор с ID, title и pdfPath
    public PdfItem(int id, String title, String pdfPath) {
        this.id = id;
        this.title = title;
        this.pdfPath = pdfPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPdfPath() {
        return pdfPath;
    }
}
