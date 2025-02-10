package com.example.app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter {
    public static final String ID_LIBRO = "idLibro";
    List<Book> books;

    public BookListAdapter(List<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_card, parent, false);
        return new MyCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyCardViewHolder viewHolder = (MyCardViewHolder) holder;
        Book book = books.get(position);
        viewHolder.getTxtTitulo().setText(book.getTitle());
        viewHolder.getView().setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), BookDetailActivity.class);
            i.putExtra(ID_LIBRO, book.getId());
            v.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
