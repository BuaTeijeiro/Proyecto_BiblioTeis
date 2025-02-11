package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import com.example.app.API.repository.BookRepository;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookListActivity extends AppCompatActivity {

    RecyclerView rv;
    TextView txtView;
    EditText etBuscador;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        inicializarViews();

        rv.setLayoutManager(new LinearLayoutManager(this));

        MainActivityVM viewModel = new ViewModelProvider(this).get(MainActivityVM.class);

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

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String criterioBusqueda = etBuscador.getText().toString();
                rv.setAdapter(new BookListAdapter(viewModel.books.getValue().stream().filter(o -> o.getTitle().contains(criterioBusqueda) || o.getAuthor().contains(criterioBusqueda)).collect(Collectors.toList())));
            }
        });

    }

    public void inicializarViews(){
        rv = findViewById(R.id.rvBookList);
        txtView = findViewById(R.id.textView);
        etBuscador = findViewById(R.id.etBuscador);
        btnBuscar = findViewById(R.id.btnBuscar);
    }
}