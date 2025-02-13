package com.example.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.API.models.User;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.UserRepository;

public class ProfileActivity extends AppCompatActivity {

    TextView tvNombreUsuario, tvEmailUsuario, tvFechaUsuario;
    UserLogIn userLogIn = new UserLogIn();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inicializarViews();
        UserRepository userRepository = new UserRepository();


        userRepository.getUserById(userLogIn.getLoggedUser().getId(), new BookRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                setUser(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ProfileActivity.this, "Fallo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUser(User userToLoad){
        if (userToLoad != null) {
            user = userToLoad;
            tvNombreUsuario.setText(userToLoad.getName());
            tvEmailUsuario.setText(userToLoad.getEmail());
            tvFechaUsuario.setText(userToLoad.getDateJoined());
        }
    }

    private void inicializarViews(){
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvFechaUsuario = findViewById(R.id.tvFechaUsuario);
    }
}