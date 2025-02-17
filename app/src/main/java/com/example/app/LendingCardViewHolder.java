package com.example.app;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class LendingCardViewHolder extends RecyclerView.ViewHolder {

    TextView txtTituloLibro, txtFechaPrestado, txtDevolucion;
    View view;

    public LendingCardViewHolder(View view){
        super(view);
        this.view = view;
        txtTituloLibro = view.findViewById(R.id.txtTituloLibro);
        txtFechaPrestado = view.findViewById(R.id.txtFechaPrestado);
        txtDevolucion = view.findViewById(R.id.txtDevolucion);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTxtTituloLibro() {
        return txtTituloLibro;
    }

    public void setTxtTituloLibro(TextView txtTituloLibro) {
        this.txtTituloLibro = txtTituloLibro;
    }

    public TextView getTxtFechaPrestado() {
        return txtFechaPrestado;
    }

    public TextView getTxtDevolucion() {
        return txtDevolucion;
    }

    public void setTxtDevolucion(TextView txtDevolucion) {
        this.txtDevolucion = txtDevolucion;
    }

    public void setTxtFechaPrestado(TextView txtFechaPrestado) {
        this.txtFechaPrestado = txtFechaPrestado;
    }
}
