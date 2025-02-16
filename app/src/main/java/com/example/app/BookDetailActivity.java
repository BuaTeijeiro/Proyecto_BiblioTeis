package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;
import com.example.app.API.repository.BookLendingRepository;
import com.example.app.API.repository.BookRepository;
import com.example.app.dto.BookLendingForm;

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

        searchBook(id);

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

        btnDevolver.setOnClickListener( v ->{
            bookLendingRepository.returnBook(book.getCurrentLending().getId(), new BookRepository.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    if (result){
                        searchBook(book.getId());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

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
        reloadViews();
    }

    private void searchBook(int id) {
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
    }

    private void reloadViews() {
        if (userLogIn.getLoggedUser() != null) {
            BookLending lending = book.getCurrentLending();
            if (book.isAvailable()) {
                btnTomarPrestado.setVisibility(View.VISIBLE);
                tvMensajePrestamo.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.GONE);
            } else if(lending!= null && lending.getUserId() == userLogIn.getLoggedUser().getId()) {
                btnTomarPrestado.setVisibility(View.GONE);
                tvMensajePrestamo.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.VISIBLE);
            } else {
                btnTomarPrestado.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.GONE);
                tvMensajePrestamo.setText("El libro se halla prestado");
                tvMensajePrestamo.setVisibility(View.VISIBLE);
            }
        } else {
            btnTomarPrestado.setVisibility(View.GONE);
            btnDevolver.setVisibility(View.GONE);
            tvMensajePrestamo.setText("Logéese para realizar préstamos");
            tvMensajePrestamo.setVisibility(View.VISIBLE);
        }
    }
}