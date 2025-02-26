package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import com.example.app.API.repository.BookRepository;


import java.util.List;
import java.util.stream.Collectors;

public class BookListActivity extends ResumableActivity {

    RecyclerView rv;
    TextView txtView;
    EditText etBuscador;
    Button btnBuscar;
    ImageButton btnCamara;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        inicializarViews();

        setSupportActionBar(toolbar);
        addMenuProvider(new MyMenuProvider(this));

        MainActivityVM viewModel = new ViewModelProvider(this).get(MainActivityVM.class);

        rv.setLayoutManager(new LinearLayoutManager(this));
        viewModel.books.observe(this, (List<Book> books) -> {
            rv.setAdapter(new BookListAdapter(books));
        });

        BookRepository repository = new BookRepository();
        repository.getBooks(new BookRepository.ApiCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> result) {
                viewModel.books.setValue(result);
                txtView.setText(String.format("Listado de libros. Total: %d libro(s)", result.size()));
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BookListActivity.this, "Ha fallado la conexión a la base da datos", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuscar.setOnClickListener(v -> {
            String criterioBusqueda = etBuscador.getText().toString();
            rv.setAdapter(new BookListAdapter(viewModel.books.getValue().stream().filter(o -> o.getTitle().contains(criterioBusqueda) || o.getAuthor().contains(criterioBusqueda)).collect(Collectors.toList())));
        });

    }

    public void inicializarViews(){
        rv = findViewById(R.id.rvBookList);
        txtView = findViewById(R.id.textView);
        etBuscador = findViewById(R.id.etBuscador);
        btnBuscar = findViewById(R.id.btnBuscar);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onResume(){
        super.onResume();
        invalidateMenu();
    }
}