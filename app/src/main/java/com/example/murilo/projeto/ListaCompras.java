package com.example.murilo.projeto;

import static android.R.attr.id;

/**
 * Created by murilo on 11/14/17.
 */

public class ListaCompras {
    private long id = 0;
    private String NomeLista;
    private String data_registro;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeLista() {
        return NomeLista;
    }

    public void setNomeLista(String nome) {
        NomeLista = nome;
    }

    public String getData_registro() {
        return data_registro;
    }

    public void setData_registro(String data_registro) {
        this.data_registro = data_registro;
    }

}
