package com.example.app;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.BookLending;
import com.example.app.API.models.User;
import com.example.app.API.repository.BookLendingRepository;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends ResumableActivity {

    TextView tvNombreUsuario, tvEmailUsuario, tvFechaUsuario;
    UserLogIn userLogIn = new UserLogIn();
    RecyclerView rvBookLendingList;
    User user;
    BookLendingRepository bookLendingRepository = new BookLendingRepository();
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

            bookLendingRepository.getAllLendings(new BookRepository.ApiCallback<List<BookLending>>() {
                @Override
                public void onSuccess(List<BookLending> result) {
                    List<BookLending> lendingsUser = result.stream().filter(o -> o.getUserId() == user.getId()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
                    viewModel.lendings.setValue(lendingsUser);
                }
                @Override
                public void onFailure(Throwable t) {
                }
            });
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