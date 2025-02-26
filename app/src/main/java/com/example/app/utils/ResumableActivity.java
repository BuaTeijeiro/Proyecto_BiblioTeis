package com.example.app.utils;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ResumableActivity extends AppCompatActivity {
    public void resume(){
        onResume();
    }
}
