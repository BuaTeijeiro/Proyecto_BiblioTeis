package com.example.app.utils;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.example.app.activities.LogInActivity;
import com.example.app.activities.QRActivity;
import com.example.app.R;

public class MyMenuProvider  implements MenuProvider {

    ResumableActivity activity;
    UserLogIn userLogIn = new UserLogIn();

    public MyMenuProvider(ResumableActivity activity){
        this.activity = activity;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.login){
            Intent i = new Intent(activity, LogInActivity.class);
            activity.startActivity(i);
            return true;
        }
        if (id == R.id.btnQR){
            activity.startActivity(new Intent(activity, QRActivity.class));
        }
        if (id == R.id.logout){
            userLogIn.setLoggedUser(null);
            activity.resume();
            return true;
        }
        return false;
    }

    @Override
    public void onPrepareMenu(@NonNull Menu menu){
        if (userLogIn.getLoggedUser() == null){
            menu.findItem(R.id.login).setVisible(true);
            menu.findItem(R.id.logout).setVisible(false);
        } else {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.logout).setVisible(true);
        }
    }


}

