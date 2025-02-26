package com.example.app.activities.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.app.API.models.Book;
import com.example.app.API.models.User;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.ImageRepository;
import com.example.app.R;
import com.example.app.activities.booklist.BookListActivity;
import com.example.app.activities.profile.ProfileActivity;
import com.example.app.utils.MyMenuProvider;
import com.example.app.utils.ResumableActivity;
import com.example.app.utils.UserLogIn;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import okhttp3.ResponseBody;


public class MainActivity extends ResumableActivity {

    Button btnListLibrons;
    Button btnPerfil;
    TextView tvSaludo, tvNombreRecomendacion, tvRecomendacion;
    ImageView imageView;
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

        searchRecommendation();

    }

    private void searchRecommendation() {
        BookRepository br = new BookRepository();
        br.getBooks(new BookRepository.ApiCallback<List<Book>>() {
            @Override
            public void onSuccess(List<Book> result) {
                if (!result.isEmpty()){
                    Optional<Book> book = result.stream().filter(o -> o.isAvailable()).findFirst();
                    setRecommendation(book);
                } else{
                    setRecommendation(Optional.empty());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                setRecommendation(Optional.empty());
            }
        });
    }

    private void setRecommendation(Optional<Book> recommendation){
        if (recommendation.isPresent()) {
            tvNombreRecomendacion.setVisibility(View.VISIBLE);
            tvRecomendacion.setText(getString(R.string.sugerancia));
            Book book = recommendation.get();
            tvNombreRecomendacion.setText(book.getTitle());
            ImageRepository imageRepository = new ImageRepository();
            imageRepository.getImage(book.getBookPicture(), new BookRepository.ApiCallback<ResponseBody>() {
                @Override
                public void onSuccess(ResponseBody result) {
                    if (result != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(result.byteStream());
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.book);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    imageView.setImageResource(R.drawable.book);
                }
            });
        }
        else {
            imageView.setImageResource(R.drawable.not_found);
            tvNombreRecomendacion.setVisibility(View.GONE);
            tvRecomendacion.setText(getString(R.string.no_book_message));
        }

    }

    private void inicializarViews() {
        btnListLibrons = findViewById(R.id.btnListaLibros);
        toolbar = findViewById(R.id.toolbar);
        tvSaludo = findViewById(R.id.tvSaludo);
        btnPerfil = findViewById(R.id.btnPerfil);
        tvNombreRecomendacion = findViewById(R.id.tvNombreRecomendacion);
        imageView = findViewById(R.id.imageView);
        tvRecomendacion = findViewById(R.id.tvRecomendacion);
    }

    @Override
    protected void onResume(){
        super.onResume();
        User usuario = userLogIn.getLoggedUser();
        invalidateMenu();
        if (usuario!=null){
            btnPerfil.setVisibility(View.VISIBLE);
            tvSaludo.setVisibility(View.VISIBLE);
            tvSaludo.setText(getString(R.string.bienvenidos, ", " + usuario.getName()));
        } else {
            btnPerfil.setVisibility(View.GONE);
            tvSaludo.setText(getString(R.string.bienvenidos, ""));
        }
        searchRecommendation();
    }
}