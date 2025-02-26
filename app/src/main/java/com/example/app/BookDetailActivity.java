package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;
import com.example.app.API.models.User;
import com.example.app.API.repository.BookLendingRepository;
import com.example.app.API.repository.BookRepository;
import com.example.app.dto.BookLendingForm;

public class BookDetailActivity extends ResumableActivity {

    TextView txtTitulo;
    TextView txtISBN;
    TextView tvMensajePrestamo;
    Button btnTomarPrestado, btnDevolver;
    UserLogIn userLogIn = new UserLogIn();
    Book book;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        inicializarViews();

        setSupportActionBar(toolbar);
        addMenuProvider(new MyMenuProvider(this));

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
                        searchBook(book.getId());
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
        toolbar = findViewById(R.id.toolbar);
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
                tvMensajePrestamo.setText(String.format("El libro se halla prestado.\n Será devuelto el día %s.\n Si ha pasado esa fecha, espera a que la biblioteca le indique que ha sido devuelto", lending.getDueDate()));
                tvMensajePrestamo.setVisibility(View.VISIBLE);
            }
        } else {
            btnTomarPrestado.setVisibility(View.GONE);
            btnDevolver.setVisibility(View.GONE);
            tvMensajePrestamo.setText("Logéese para realizar préstamos");
            tvMensajePrestamo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        reloadViews();
    }
}