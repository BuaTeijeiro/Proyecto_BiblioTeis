package com.example.app.activities.booklist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.API.models.Book;

import java.util.List;

public class BookListActivityVM extends ViewModel {

    MutableLiveData<List<Book>> books;

    public BookListActivityVM(){
        books = new MutableLiveData<>();
    }
}
