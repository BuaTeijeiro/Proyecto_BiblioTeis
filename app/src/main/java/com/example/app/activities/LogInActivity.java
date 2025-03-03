package com.example.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.app.API.models.User;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.UserRepository;
import com.example.app.R;
import com.example.app.utils.UserLogIn;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LogInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnCancelarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        inicializarViews();

        UserRepository userRepository = new UserRepository();

        btnLogin.setOnClickListener(v -> {
            if (etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
                Toast.makeText(this, "El email y la contraseña son obligatorios", Toast.LENGTH_SHORT).show();
            } else {
                userRepository.checkUser(etEmail.getText().toString(), etPassword.getText().toString(), new BookRepository.ApiCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        if (result == null){
                            Toast.makeText(LogInActivity.this, "El usuario y contraseña no coinciden", Toast.LENGTH_SHORT).show();
                            etPassword.setText("");
                        } else {
                            UserLogIn userLogIn = new UserLogIn();
                            userLogIn.loggearUsuario(result);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(LogInActivity.this, "El usuario y contraseña no coinciden", Toast.LENGTH_SHORT).show();
                        etPassword.setText("");
                    }
                });
            }
        });

        btnCancelarLogin.setOnClickListener(v->{
            finish();
        });
    }

    public void inicializarViews(){
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword.setText("hashedpassword2");
        etEmail.setText("bob@example.com");
        btnLogin = findViewById(R.id.btnCheckLogin);
        btnCancelarLogin = findViewById(R.id.btnCancelarLogin);
    }
}