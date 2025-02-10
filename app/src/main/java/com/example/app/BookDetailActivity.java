package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.API.models.Book;
import com.example.app.API.repository.BookRepository;

public class BookDetailActivity extends AppCompatActivity {

    TextView txtTitulo;
    TextView txtISBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        inicializarViews();

        Intent i = getIntent();
        int id = i.getIntExtra(BookListAdapter.ID_LIBRO, 0);

        BookRepository bookRepository = new BookRepository();

        bookRepository.getBookById(id, new BookRepository.ApiCallback<Book>() {
            @Override
            public void onSuccess(Book result) {
                setBook(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BookDetailActivity.this, "No se ha podido cargar el libro", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void inicializarViews(){
        txtTitulo = findViewById(R.id.txtTitulo);
        txtISBN = findViewById(R.id.txtISBN);
    }

    public void setBook(Book book){
        txtTitulo.setText(book.getTitle());
        txtISBN.setText(book.getIsbn());
    }
}