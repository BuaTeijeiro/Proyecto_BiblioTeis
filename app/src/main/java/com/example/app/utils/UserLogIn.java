package com.example.app.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.app.API.models.User;
import com.example.app.API.repository.BookRepository;
import com.example.app.API.repository.UserRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class UserLogIn {
    private static final String TOKEN = "token";
    private static MutableLiveData<User> loggedUser = new MutableLiveData<>();
    public static SharedPreferences spEncrypted;

    public MutableLiveData<User> getMutableData(){
        return loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser.getValue();
    }

    void setLoggedUser(User loggedUser) {
        UserLogIn.loggedUser.setValue(loggedUser);
    }

    public void loggearUsuario(User usuario){
        int userId = usuario != null ? usuario.getId() : 0;
        if (spEncrypted!=null) {
            SharedPreferences.Editor ed = spEncrypted.edit();
            ed.putString(TOKEN, String.valueOf(userId));
            ed.apply();
        }
        setLoggedUser(usuario);
    }

    public void logout(){
        loggearUsuario(null);
    }

    public void initialize(Context context){
        MasterKey mk = null;
        try{
            mk = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            spEncrypted = EncryptedSharedPreferences.create( context, "ECRYPTEDSHARE", mk, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            getSavedUser();
        } catch (GeneralSecurityException | IOException e){
            Toast.makeText(context, "Error al registrar el login", Toast.LENGTH_SHORT).show();
        }

    }

    private void getSavedUser(){
        String valor = spEncrypted.getString(TOKEN, null);
        if (valor != null){
            UserRepository ur = new UserRepository();
            ur.getUserById(Integer.valueOf(valor), new BookRepository.ApiCallback<User>() {
                @Override
                public void onSuccess(User result) {
                    setLoggedUser(result);
                }

                @Override
                public void onFailure(Throwable t) {
                    setLoggedUser(null);
                }
            });
        }
    }
}
