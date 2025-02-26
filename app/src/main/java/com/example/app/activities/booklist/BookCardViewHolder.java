package com.example.app.activities.booklist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;

public class BookCardViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitulo, txtAutor, txtFechaPublicacion;
    ImageView imgLibro;
    View view;

    public BookCardViewHolder(View view){
        super(view);
        this.view = view;
        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtAutor = view.findViewById(R.id.txtAutor);
        txtFechaPublicacion = view.findViewById(R.id.txtFechaPublicacion);
        imgLibro = view.findViewById(R.id.imgLibro);
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

    public TextView getTxtAutor() {
        return txtAutor;
    }

    public void setTxtAutor(TextView txtAutor) {
        this.txtAutor = txtAutor;
    }

    public TextView getTxtFechaPublicacion() {
        return txtFechaPublicacion;
    }

    public void setTxtFechaPublicacion(TextView txtFechaPublicacion) {
        this.txtFechaPublicacion = txtFechaPublicacion;
    }

    public ImageView getImgLibro() {
        return imgLibro;
    }

    public void setImgLibro(ImageView imgLibro) {
        this.imgLibro = imgLibro;
    }
}
