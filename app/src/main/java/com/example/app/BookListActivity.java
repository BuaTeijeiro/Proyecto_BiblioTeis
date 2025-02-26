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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import com.example.app.API.repository.BookRepository;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.List;
import java.util.stream.Collectors;

public class BookListActivity extends ResumableActivity {

    RecyclerView rv;
    TextView txtView;
    EditText etBuscador;
    Button btnBuscar;
    ImageButton btnCamara;
    Toolbar toolbar;
    ActivityResultLauncher<Uri> intentQR;
    UserLogIn userLogIn = new UserLogIn();

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
                Toast.makeText(BookListActivity.this, "No se han encontrado libros", Toast.LENGTH_SHORT).show();
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
}