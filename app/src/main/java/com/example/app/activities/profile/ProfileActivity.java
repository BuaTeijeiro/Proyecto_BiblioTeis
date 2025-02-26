package com.example.app.activities.profile;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.BookLending;
import com.example.app.API.models.User;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.UserRepository;
import com.example.app.R;
import com.example.app.utils.MyMenuProvider;
import com.example.app.utils.ResumableActivity;
import com.example.app.utils.UserLogIn;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends ResumableActivity {

    TextView tvNombreUsuario, tvEmailUsuario, tvFechaUsuario;
    UserLogIn userLogIn = new UserLogIn();
    RecyclerView rvBookLendingList;
    User user;
    ProfileVM viewModel;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        inicializarViews();
        setSupportActionBar(toolbar);
        addMenuProvider(new MyMenuProvider(this));

        UserRepository userRepository = new UserRepository();

        viewModel = new ViewModelProvider(this).get(ProfileVM.class);
        rvBookLendingList.setLayoutManager(new LinearLayoutManager(this));
        viewModel.lendings.observe(this, (List<BookLending> lendings) -> {
            rvBookLendingList.setAdapter(new LendingListAdapter(lendings));
        });

        userRepository.getUserById(userLogIn.getLoggedUser().getId(), new BookRepository.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                setUser(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error al cargar al usuario", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void setUser(User userToLoad){
        if (userToLoad != null) {
            user = userToLoad;
            tvNombreUsuario.setText(getString(R.string.nombre_usuario,userToLoad.getName()));
            tvEmailUsuario.setText(getString(R.string.email_usuario,userToLoad.getEmail()));
            tvFechaUsuario.setText(getString(R.string.fecha_de_registro_usuario,userToLoad.getDateJoined().substring(0,10)));
            viewModel.lendings.setValue(user.getBookLendings().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (userLogIn.getLoggedUser() == null){
            finish();
        }
    }

    private void inicializarViews(){
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvFechaUsuario = findViewById(R.id.tvFechaUsuario);
        rvBookLendingList = findViewById(R.id.rvBookLendingList);
        toolbar = findViewById(R.id.toolbar);
    }
}