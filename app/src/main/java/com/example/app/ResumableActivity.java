package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ResumableActivity extends AppCompatActivity {
    public void resume(){
        onResume();
    }
}
