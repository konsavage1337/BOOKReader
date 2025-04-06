package com.example.bookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context, List<Book> bookList, OnItemClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);

        if (book == null) {
            return; // Пропускаем null-объекты
        }

        // Проверяем и устанавливаем название книги
        holder.title.setText(book.getTitle() != null ? book.getTitle() : "Без названия");

        // Проверяем и устанавливаем автора
        holder.author.setText("Автор: " + (book.getAuthor() != null ? book.getAuthor() : "Неизвестен"));

        // Проверяем и устанавливаем дату публикации
        holder.date.setText("Дата издания: " + (book.getDate() != null ? book.getDate() : "Не указана"));

        // Проверяем изображение и загружаем его, если оно не null
        String imageName = book.getImage();
        if (imageName != null) {
            int imageId = context.getResources().getIdentifier(imageName.replace(".png", ""), "drawable", context.getPackageName());
            if (imageId != 0) {
                holder.image.setImageResource(imageId);
            } else {
                holder.image.setImageResource(R.drawable.default_book_image); // Установи изображение по умолчанию
            }
        } else {
            holder.image.setImageResource(R.drawable.default_book_image);
        }

        // Обработчик клика
        holder.itemView.setOnClickListener(v -> listener.onItemClick(book));
    }

    @Override
    public int getItemCount() {
        return bookList != null ? bookList.size() : 0;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, date;
        ImageView image;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            date = itemView.findViewById(R.id.book_date);
            image = itemView.findViewById(R.id.book_image);
        }
    }
}