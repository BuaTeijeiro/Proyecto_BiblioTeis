package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.API.models.User;


public class MainActivity extends AppCompatActivity {

    Button btnListLibrons;
    Button btnLogin, btnLogOut, btnPerfil;
    TextView tvBienvenida;
    UserLogIn userLogIn = new UserLogIn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarViews();

        btnListLibrons.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), BookListActivity.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), LogInActivity.class);
            startActivity(i);
        });

        btnLogOut.setOnClickListener(v ->{
            userLogIn.setLoggedUser(null);
            onResume();
        });

        btnPerfil.setOnClickListener(v->{
            Intent i = new Intent(v.getContext(), ProfileActivity.class);
            startActivity(i);
        });

    }

    private void inicializarViews() {
        btnListLibrons = findViewById(R.id.btnListaLibros);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogOut = findViewById(R.id.btnLogOut);
        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnPerfil = findViewById(R.id.btnPerfil);
    }

    @Override
    protected void onResume(){
        super.onResume();
        User usuario;
        if ((usuario = userLogIn.getLoggedUser())!=null){
            btnLogin.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.VISIBLE);
            btnPerfil.setVisibility(View.VISIBLE);
            tvBienvenida.setVisibility(View.VISIBLE);
            tvBienvenida.setText("Bienvenido " + usuario.getName());
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnPerfil.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.GONE);
            tvBienvenida.setVisibility(View.GONE);
        }
    }
}