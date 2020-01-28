package com.example.murilo.projeto;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditListCompras extends Fragment {

    private ListaCompras lc = new ListaCompras();
    private Banco banco;
    private ArrayList<Produto> produto = null;
    private AdpProduto AdpProduto = null;
    private ListView lv_edit_produto;

    private EditText et_edit_produto = null;
    private EditText et_edit_qtde = null;
    private EditText et_edit_lista_name = null;
    private FloatingActionButton btn_edit_add = null;
    private FloatingActionButton btn_edit_save = null;
    private ArrayList<Produto> produto_db = null;
    private Produto p = new Produto();

    public EditListCompras() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.fragment_edit_list_compras, container, false);
        et_edit_produto = (EditText) v.findViewById(R.id.et_edit_descricao);
        et_edit_qtde = (EditText) v.findViewById(R.id.et_edit_qtde);
        et_edit_lista_name = (EditText) v.findViewById(R.id.et_edit_lista_name);

        btn_edit_add = (FloatingActionButton) v.findViewById(R.id.btn_edit_cadastrar);
        btn_edit_save = (FloatingActionButton) v.findViewById(R.id.btn_edit_save);

        lv_edit_produto = (ListView)v.findViewById(R.id.lv_edit_produtos);
        produto = new ArrayList<Produto>();
        AdpProduto = new AdpProduto(getContext(), R.layout.listview_produto,produto);
        lv_edit_produto.setAdapter(AdpProduto);
        registerForContextMenu(lv_edit_produto);

        produto_db = new ArrayList<Produto>();

        banco = new Banco(this.getContext());

        btn_edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_edit_produto.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Preencha o campo produto corretamente", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_edit_qtde.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "A quantidade não pode ser vazia", Toast.LENGTH_SHORT).show();
                    return;
                }

                p.setProduto(et_edit_produto.getText().toString());
                p.setQuantidade(Integer.parseInt(et_edit_qtde.getText().toString()));
                produto.add(p);
                produto_db.add(p);
                AdpProduto.notifyDataSetChanged();
                et_edit_produto.setText("");
                et_edit_qtde.setText("");

            }
        });


        btn_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = banco.getWritableDatabase();

                if (lc.getId() == 0){
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String dataFormatada = df.format(date);


                    lc.setData_registro(dataFormatada);
                    lc.setNomeLista(et_edit_lista_name.getText().toString());

                    ContentValues lista_compra = new ContentValues();

                    lista_compra.put(EsquemaBanco.EsquemaLista.coluna_nome, lc.getNomeLista());
                    lista_compra.put(EsquemaBanco.EsquemaLista.conluna_data, lc.getData_registro());
                    long newRowId = db.insert(EsquemaBanco.EsquemaLista.table_lista, "", lista_compra);

                    lc.setId(newRowId);
//                    Toast.makeText(getContext(), "id da lista: "+ lc.getId(), Toast.LENGTH_SHORT).show();
                }


                ContentValues item = new ContentValues();

                for (int i = 0; i < produto_db.size(); i++){
                    item.put(EsquemaBanco.EsquemaProduto.coluna_descricao, produto_db.get(i).getProduto());
                    item.put(EsquemaBanco.EsquemaProduto.conluna_qtde, produto_db.get(i).getQuantidade());
                    item.put(EsquemaBanco.EsquemaProduto.coluna_list_id, lc.getId());
                    long newRowProduto = db.insert(EsquemaBanco.EsquemaProduto.table_produto, null, item);
                }
                produto_db.clear();

                Cursor read = db.rawQuery("select p.descricao, p.quantidade from Produto as p inner join ListaCompras as lc on p.list_id = lc._id and p.list_id = " + lc.getId(), null);

                read.moveToFirst();
                produto.clear();
                while (read.isAfterLast() == false){
                    Produto p_list = new Produto();

                    String item_p = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaProduto.coluna_descricao));
                    String qtde = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaProduto.conluna_qtde));
                    p_list.setProduto(item_p);
                    p_list.setQuantidade(Integer.parseInt(qtde));
                    produto.add(p_list);

                    read.moveToNext();
                }

                AdpProduto.notifyDataSetChanged();
                et_edit_produto.setText("");
                et_edit_qtde.setText("");

                Toast.makeText(getContext(), "Lista cadastrada com sucesso", Toast.LENGTH_LONG).show();

            }
        });


        return v;
    }

    public void getListaCompras (int lista)
    {

        lc.setId(lista);

        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor read = db.rawQuery("select lc._id, p.descricao, p.quantidade, lc.nome from Produto as p inner join ListaCompras as lc on p.list_id = lc._id and p.list_id = " + lc.getId(), null);
//        Toast.makeText(getContext(),"id da lista " + lc.getId(), Toast.LENGTH_LONG).show();
        read.moveToFirst();
        produto.clear();

        while (read.isAfterLast() == false)
        {
            Produto p_list = new Produto();
            Integer id = read.getInt(read.getColumnIndex(EsquemaBanco.EsquemaLista._ID));

            String item = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaProduto.coluna_descricao));
            String qtde = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaProduto.conluna_qtde));
            String lista_name = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaLista.coluna_nome));
            p_list.setProduto(item);
            p_list.setQuantidade(Integer.parseInt(qtde));
            produto.add(p_list);
            lc.setNomeLista(lista_name);
            read.moveToNext();
        }
        et_edit_lista_name.setText(lc.getNomeLista());
        AdpProduto.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi = getActivity().getMenuInflater();
        mi.inflate(R.menu.menu_contexto,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.m_dell:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja realmente deletar esse produto ?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(getContext(), "Nome produto: " +  produto.get(info.position).getProduto(), Toast.LENGTH_LONG).show();

                        SQLiteDatabase db = banco.getReadableDatabase();
                        Cursor c = db.rawQuery("Select _id from Produto where descricao='"+ produto.get(info.position).getProduto() + "'", null);
                        c.moveToFirst();
                        do {
                            long id = c.getInt(c.getColumnIndex(EsquemaBanco.EsquemaProduto._ID));
                            p.setId((int) id);

                        }while (c.moveToNext());

                        SQLiteDatabase bd = banco.getWritableDatabase();
                        bd.delete(EsquemaBanco.EsquemaProduto.table_produto, "_id = ? and list_id = ? ", new String[]{String.valueOf(p.getId()), String.valueOf(lc.getId())});
                        produto.remove(info.position);
                        AdpProduto.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            default: return super.onContextItemSelected(item);
        }

    }
}
