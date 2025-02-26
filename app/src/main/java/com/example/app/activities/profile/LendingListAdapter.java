package com.example.app.activities.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;
import com.example.app.API.repository.BookRepository;
import com.example.app.R;

import java.util.List;

public class LendingListAdapter extends RecyclerView.Adapter {
    List<BookLending> lendings;
    BookRepository bookRepository = new BookRepository();

    public LendingListAdapter(List<BookLending> lendings){
        this.lendings = lendings;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lending, parent, false);
        return new LendingCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LendingCardViewHolder viewHolder = (LendingCardViewHolder) holder;
        BookLending lending = lendings.get(position);
        viewHolder.getTxtFechaPrestado().setText(lending.getDateString(lending.getLendDate()));
        if (lending.getReturnDate() == null) {
            if (lending.isLate()){
                viewHolder.getTxtDevolucion().setText("El libro tendría que haber sido devuelto en la fecha " + lending.getDueDate());
                viewHolder.getView().setBackgroundColor(viewHolder.getView().getResources().getColor(R.color.red));
            } else {
                viewHolder.getTxtDevolucion().setText("El libro debe ser devuelto antes de la fecha " + lending.getDueDate());
                viewHolder.getView().setBackgroundColor(viewHolder.getView().getResources().getColor(R.color.green));
            }
        } else {
            viewHolder.getTxtDevolucion().setText("El libro fue devuelto en la fecha " + lending.getDateString(lending.getReturnDate()));
            viewHolder.getView().setBackgroundColor(viewHolder.getView().getResources().getColor(R.color.blue));

        }
        bookRepository.getBookById(lending.getBookId(), new BookRepository.ApiCallback<Book>() {
            @Override
            public void onSuccess(Book result) {
                viewHolder.getTxtTituloLibro().setText(result.getTitle());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lendings.size();
    }
}
