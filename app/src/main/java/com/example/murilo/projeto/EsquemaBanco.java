package com.example.murilo.projeto;

import android.provider.BaseColumns;

/**
 * Created by murilo on 11/14/17.
 */

public class EsquemaBanco  {

    private EsquemaBanco(){ }

    public static class EsquemaProduto implements BaseColumns
    {
        public static final String table_produto = "Produto";
        public static final String coluna_descricao = "descricao";
        public static final String conluna_qtde = "quantidade";
        public static final String coluna_list_id = "list_id";

    }

    public static class EsquemaLista implements BaseColumns
    {
        public static final String table_lista = "ListaCompras";
        public static final String coluna_nome = "nome";
        public static final String conluna_data = "data_registro";
    }
}
