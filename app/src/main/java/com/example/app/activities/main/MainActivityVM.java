package com.example.app.activities.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.API.models.Book;

import java.util.List;

public class MainActivityVM extends ViewModel {

    MutableLiveData<List<Book>> books;

    public MainActivityVM(){
        books = new MutableLiveData<>();
    }
}
