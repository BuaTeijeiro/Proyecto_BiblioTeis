package com.example.app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;
import com.example.app.API.models.User;
import com.example.app.API.repository.BookLendingRepository;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends AppCompatActivity {

    TextView tvNombreUsuario, tvEmailUsuario, tvFechaUsuario;
    UserLogIn userLogIn = new UserLogIn();
    RecyclerView rvBookLendingList;
    User user;
    BookLendingRepository bookLendingRepository = new BookLendingRepository();
    ProfileVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inicializarViews();
        UserRepository userRepository = new UserRepository();

        viewModel = new ViewModelProvider(this).get(ProfileVM.class);
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
                    List<BookLending> lendingsUser = result.stream().filter(o -> o.getUserId() == user.getId()).collect(Collectors.toList());
                    viewModel.lendings.setValue(lendingsUser);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });

        }
    }

    private void inicializarViews(){
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvFechaUsuario = findViewById(R.id.tvFechaUsuario);
        rvBookLendingList = findViewById(R.id.rvBookLendingList);
    }
}