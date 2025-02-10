package com.example.app;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appbiblioteis.API.models.Book;

public class MainActivityVM extends ViewModel {

    MutableLiveData<Book> book;

    public MainActivityVM(){
        book = new MutableLiveData<>();
    }



}
