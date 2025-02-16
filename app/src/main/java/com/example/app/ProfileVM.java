package com.example.app;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.API.models.Book;
import com.example.app.API.models.BookLending;

import java.util.List;

public class ProfileVM extends ViewModel {

    MutableLiveData<List<BookLending>> lendings;

    public ProfileVM(){
        lendings = new MutableLiveData<>();
    }
}
