package com.example.murilo.projeto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by murilo on 11/14/17.
 */

public class Banco extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "compra_certa.db";

    private String create_lista = "create table " + EsquemaBanco.EsquemaLista.table_lista
            + "(" + EsquemaBanco.EsquemaLista._ID + " integer primary key, "
            + EsquemaBanco.EsquemaLista.coluna_nome + " varchar(20), "
            + EsquemaBanco.EsquemaLista.conluna_data + " varchar(15) "
            + ");";

    private String create_produto = "create table " + EsquemaBanco.EsquemaProduto.table_produto
                                    + "(" + EsquemaBanco.EsquemaProduto._ID + " integer primary key, "
                                    + EsquemaBanco.EsquemaProduto.coluna_descricao + " varchar(100), "
                                    + EsquemaBanco.EsquemaProduto.conluna_qtde + " varchar(10), "
                                    + EsquemaBanco.EsquemaProduto.coluna_list_id + " integer(10), "
                                    + " foreign key (" + EsquemaBanco.EsquemaProduto.coluna_list_id +")"
                                    + " references "+ EsquemaBanco.EsquemaLista.table_lista + " (" + EsquemaBanco.EsquemaLista._ID + ")"
                                    +");";

    private String delete_lista = " drop table if not exists " + EsquemaBanco.EsquemaLista.table_lista;
    private String delete_produto = "drop table if not exists" + EsquemaBanco.EsquemaProduto.table_produto;

    public Banco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_lista);
        db.execSQL(create_produto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(delete_produto);
        db.execSQL(delete_lista);
    }
}
