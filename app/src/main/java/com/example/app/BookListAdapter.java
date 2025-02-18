package com.example.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.ImageRepository;

import java.util.List;

import okhttp3.ResponseBody;

public class BookListAdapter extends RecyclerView.Adapter {
    public static final String ID_LIBRO = "idLibro";
    List<Book> books;
    ImageRepository imageRepository = new ImageRepository();


    public BookListAdapter(List<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book_card, parent, false);
        return new BookCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BookCardViewHolder viewHolder = (BookCardViewHolder) holder;
        Book book = books.get(position);
        viewHolder.getTxtTitulo().setText(book.getTitle());
        viewHolder.getTxtAutor().setText(book.getAuthor());
        viewHolder.getTxtFechaPublicacion().setText(book.getPublishedDate());
        imageRepository.getImage(book.getBookPicture(), new BookRepository.ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody result) {
                if (result!=null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(result.byteStream());
                    viewHolder.getImgLibro().setImageBitmap(bitmap);
                } else{
                    viewHolder.getImgLibro().setImageResource(R.drawable.not_found);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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
