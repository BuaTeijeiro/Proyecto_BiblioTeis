package com.example.app.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.app.API.models.User;
import com.example.app.R;
import com.example.app.activities.booklist.BookListActivity;
import com.example.app.activities.profile.ProfileActivity;
import com.example.app.utils.MyMenuProvider;
import com.example.app.utils.ResumableActivity;
import com.example.app.utils.UserLogIn;


public class MainActivity extends ResumableActivity {

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
        User usuario = userLogIn.getLoggedUser();
        invalidateMenu();
        if (usuario!=null){
            btnPerfil.setVisibility(View.VISIBLE);
            tvBienvenida.setVisibility(View.VISIBLE);
            tvBienvenida.setText("Bienvenido " + usuario.getName());
        } else {
            btnPerfil.setVisibility(View.GONE);
            tvBienvenida.setVisibility(View.GONE);
        }
    }
}