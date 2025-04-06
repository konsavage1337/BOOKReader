package com.example.bookreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PdfAdapter extends ArrayAdapter<PdfItem> {

    private Context context;
    private ArrayList<PdfItem> pdfItems;

    public PdfAdapter(Context context, ArrayList<PdfItem> pdfItems) {
        super(context, 0, pdfItems);
        this.context = context;
        this.pdfItems = pdfItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false);
        }

        PdfItem pdfItem = pdfItems.get(position);

        TextView titleTextView = convertView.findViewById(R.id.pdfTitle);
        titleTextView.setText(pdfItem.getTitle());

        // Обработчик кнопки "Открыть"
        Button openButton = convertView.findViewById(R.id.openButton);
        openButton.setOnClickListener(v -> {
            // Открываем PDF с помощью Intent
            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra("pdf_path", pdfItem.getPdfPath());
            intent.putExtra("pdfTitle", pdfItem.getTitle());
            context.startActivity(intent);
        });

        // Обработчик кнопки "Удалить"
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            // Удаляем PDF из базы данных по ID
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.deletePdfItem(pdfItem.getId()); // передаем ID, а не объект

            // Обновляем список после удаления
            pdfItems.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
