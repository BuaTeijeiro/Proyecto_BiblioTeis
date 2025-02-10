package com.example.app;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyCardViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitulo;
    View view;

    public MyCardViewHolder(View view){
        super(view);
        this.view = view;
        txtTitulo = view.findViewById(R.id.txtTitulo);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTxtTitulo() {
        return txtTitulo;
    }

    public void setTxtTitulo(TextView txtTitulo) {
        this.txtTitulo = txtTitulo;
    }

}
