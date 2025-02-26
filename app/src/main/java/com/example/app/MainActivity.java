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

import com.example.app.API.models.User;


public class MainActivity extends ResumableActivity{

    Button btnListLibrons;
    Button btnPerfil;
    TextView tvBienvenida;
    Toolbar toolbar;
    UserLogIn userLogIn = new UserLogIn();
    MyMenuProvider myMenuProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarViews();

        setSupportActionBar(toolbar);

        myMenuProvider = new MyMenuProvider(this);

        addMenuProvider(myMenuProvider);

        btnListLibrons.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), BookListActivity.class);
            startActivity(i);
        });

        btnPerfil.setOnClickListener(v->{
            Intent i = new Intent(v.getContext(), ProfileActivity.class);
            startActivity(i);
        });

    }

    private void inicializarViews() {
        btnListLibrons = findViewById(R.id.btnListaLibros);
        toolbar = findViewById(R.id.toolbar);
        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnPerfil = findViewById(R.id.btnPerfil);
    }

    @Override
    protected void onResume(){
        super.onResume();
        myMenuProvider.onPrepareMenu(toolbar.getMenu());
        User usuario;
        if ((usuario = userLogIn.getLoggedUser())!=null){
            //toolbar.getMenu().getItem(1).setVisible(false);
            //toolbar.getMenu().getItem(2).setVisible(true);
            btnPerfil.setVisibility(View.VISIBLE);
            tvBienvenida.setVisibility(View.VISIBLE);
            tvBienvenida.setText("Bienvenido " + usuario.getName());
        } else {
            //toolbar.getMenu().getItem(1).setVisible(true);
            //toolbar.getMenu().getItem(2).setVisible(false);
            btnPerfil.setVisibility(View.GONE);
            tvBienvenida.setVisibility(View.GONE);
        }
    }
}