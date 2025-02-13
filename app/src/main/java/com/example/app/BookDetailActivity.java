package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;
import com.example.app.API.models.User;
import com.example.app.API.repository.BookLendingRepository;
import com.example.app.API.repository.BookRepository;
import com.example.app.dto.BookLendingForm;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    TextView txtTitulo;
    TextView txtISBN;
    TextView tvMensajePrestamo;
    Button btnTomarPrestado, btnDevolver;
    UserLogIn userLogIn = new UserLogIn();
    Book book;

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
                finish();
            }
        });

        BookLendingRepository bookLendingRepository = new BookLendingRepository();

        btnTomarPrestado.setOnClickListener( v -> {
            BookLendingForm bookLendingForm = new BookLendingForm(book.getId(), userLogIn.getLoggedUser().getId());
            bookLendingRepository.lendBook(bookLendingForm, new BookRepository.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    if (result){
                        btnTomarPrestado.setVisibility(View.GONE);
                        btnDevolver.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(BookDetailActivity.this, "La API ha devuelto false", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(BookDetailActivity.this, "Error en la APi", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    public void inicializarViews(){
        txtTitulo = findViewById(R.id.txtTitulo);
        txtISBN = findViewById(R.id.txtISBN);
        btnDevolver = findViewById(R.id.btnDevolver);
        btnTomarPrestado = findViewById(R.id.btnTomarPrestado);
        tvMensajePrestamo = findViewById(R.id.tvMensajePrestamo);
    }

    public void setBook(Book book){
        this.book = book;
        txtTitulo.setText(book.getTitle());
        txtISBN.setText(book.getIsbn());
        if (userLogIn.getLoggedUser() != null) {
            tvMensajePrestamo.setVisibility(View.GONE);
            if (book.isAvailable()) {
                btnTomarPrestado.setVisibility(View.VISIBLE);
                btnDevolver.setVisibility(View.GONE);
            } else {
                btnTomarPrestado.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.GONE);

            }
        } else {
            btnTomarPrestado.setVisibility(View.GONE);
            btnDevolver.setVisibility(View.GONE);
            tvMensajePrestamo.setVisibility(View.VISIBLE);
        }

    }
}