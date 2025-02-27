package com.example.app.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;
import com.example.app.API.repository.BookLendingRepository;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.ImageRepository;
import com.example.app.activities.booklist.BookListAdapter;
import com.example.app.R;
import com.example.app.dto.BookLendingForm;
import com.example.app.utils.MyMenuProvider;
import com.example.app.utils.ResumableActivity;
import com.example.app.utils.UserLogIn;

import okhttp3.ResponseBody;

public class BookDetailActivity extends ResumableActivity {
    public static final String ID_LIBRO = "idLibro";
    TextView txtTitulo, txtISBN, txtAutor, txtFechaPublicacion;
    TextView tvMensajePrestamo;
    ImageView imageDetalle;
    Button btnTomarPrestado, btnDevolver;
    UserLogIn userLogIn = new UserLogIn();
    Book book;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        inicializarViews();

        setSupportActionBar(toolbar);
        addMenuProvider(new MyMenuProvider(this));

        int id = getIntent().getIntExtra(ID_LIBRO, 0);
        searchBook(id);

        BookLendingRepository bookLendingRepository = new BookLendingRepository();
        btnTomarPrestado.setOnClickListener( v -> {
            BookLendingForm bookLendingForm = new BookLendingForm(book.getId(), userLogIn.getLoggedUser().getId());
            bookLendingRepository.lendBook(bookLendingForm, new BookRepository.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    if (result){
                        book.setAvailable(false);
                        BookLending bookLending = new BookLending();
                        bookLending.setUserId(userLogIn.getLoggedUser().getId());
                        book.getBookLendings().add(bookLending);
                    } else {
                        Toast.makeText(BookDetailActivity.this, "No se ha podido reservar el libro", Toast.LENGTH_SHORT).show();
                    }
                    reloadViews();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(BookDetailActivity.this, "Error en la APi", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnDevolver.setOnClickListener( v ->{
            bookLendingRepository.returnBook(book.getCurrentLending().getId(), new BookRepository.ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    if (result){
                        book.getCurrentLending().setLendDate("hoy");
                        book.setAvailable(true);
                    } else {
                        Toast.makeText(BookDetailActivity.this, "No se ha podido devolver el libro", Toast.LENGTH_SHORT).show();
                    }
                    reloadViews();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        });

    }

    public void inicializarViews(){
        txtTitulo = findViewById(R.id.txtTitulo);
        txtISBN = findViewById(R.id.txtISBN);
        txtAutor = findViewById(R.id.txtAutor);
        txtFechaPublicacion = findViewById(R.id.txtFechaPublicacion);
        imageDetalle = findViewById(R.id.imageDetalle);
        btnDevolver = findViewById(R.id.btnDevolver);
        btnTomarPrestado = findViewById(R.id.btnTomarPrestado);
        tvMensajePrestamo = findViewById(R.id.tvMensajePrestamo);
        toolbar = findViewById(R.id.toolbar);
    }

    public void setBook(Book book){
        this.book = book;
        txtTitulo.setText(getString(R.string.titulo_libro, book.getTitle()));
        txtAutor.setText(getString(R.string.autor_libro, book.getAuthor()));
        txtFechaPublicacion.setText(getString(R.string.fecha_publicacion_libro, book.getPublishedDate().substring(0,10)));
        txtISBN.setText(getString(R.string.isbn_libro, book.getIsbn()));
        ImageRepository imageRepository = new ImageRepository();
        imageRepository.getImage(book.getBookPicture(), new BookRepository.ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody result) {
                if (result != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(result.byteStream());
                    imageDetalle.setImageBitmap(bitmap);
                } else {
                    imageDetalle.setImageResource(R.drawable.book);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                imageDetalle.setImageResource(R.drawable.book);
            }
        });
        reloadViews();
    }

    private void searchBook(int id) {
        BookRepository bookRepository = new BookRepository();
        bookRepository.getBookById(id, new BookRepository.ApiCallback<Book>() {
            @Override
            public void onSuccess(Book result) {
                setBook(result);
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(BookDetailActivity.this, "No se ha podido cargar el libro", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void reloadViews() {
        if (userLogIn.getLoggedUser() != null) {
            BookLending lending = book.getCurrentLending();
            if (book.isAvailable()) {
                btnTomarPrestado.setVisibility(View.VISIBLE);
                tvMensajePrestamo.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.GONE);
            } else if(lending!= null && lending.getUserId() == userLogIn.getLoggedUser().getId()) {
                btnTomarPrestado.setVisibility(View.GONE);
                tvMensajePrestamo.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.VISIBLE);
            } else {
                btnTomarPrestado.setVisibility(View.GONE);
                btnDevolver.setVisibility(View.GONE);
                tvMensajePrestamo.setText(String.format("El libro se halla prestado.\n Será devuelto el día %s.\n Si ha pasado esa fecha, espera a que la biblioteca le indique que ha sido devuelto", lending.getDueDate()));
                tvMensajePrestamo.setVisibility(View.VISIBLE);
            }
        } else {
            btnTomarPrestado.setVisibility(View.GONE);
            btnDevolver.setVisibility(View.GONE);
            tvMensajePrestamo.setText("Logéese para realizar préstamos");
            tvMensajePrestamo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        invalidateMenu();
        if (book!=null) {
            reloadViews();
        }
    }
}