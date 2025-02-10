package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button btnListLibrons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListLibrons = findViewById(R.id.btnListaLibros);

        btnListLibrons.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), BookListActivity.class);
            startActivity(i);
        });

        /*
        MainActivityVM vm = new ViewModelProvider(this).get(MainActivityVM.class);

        vm.book.observe(this, (Book book) -> {
            ((TextView)findViewById(R.id.name)).setText(book.getTitle());
            ((TextView)findViewById(R.id.Autor)).setText(book.getAuthor());
        });

        BookRepository br = new BookRepository();

        br.getBookById(1, new BookRepository.ApiCallback<Book>() {
            @Override
            public void onSuccess(Book result) {
                vm.book.setValue(result);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/

    }
}