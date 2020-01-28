package com.example.murilo.projeto;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.id;
import static java.util.Objects.isNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutoCadastro extends Fragment {

    private EditText et_produto = null;
    private EditText et_qtde = null;
    private EditText et_nome_lista_compras = null;
    private FloatingActionButton btn_add = null;
    private FloatingActionButton btn_save_list = null;

    /*ListView*/
    private ArrayList<Produto> produtos = null;
    private ArrayList<Produto> produtos_db = null;
    private AdpProduto adpProduto = null;
    private ListView lv_produtos = null;

    private Banco banco;
    //private long id = 0;
    private Produto p;
    private ListaCompras lc = new ListaCompras();

    public ProdutoCadastro() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_produto_cadastro, container, false);

        et_produto = (EditText)v.findViewById(R.id.txt_produto);
        et_qtde = (EditText)v.findViewById(R.id.txt_qtde);
        et_nome_lista_compras = (EditText)v.findViewById(R.id.txt_lista);
        btn_add = (FloatingActionButton) v.findViewById(R.id.btn_cadastrar);
        btn_save_list = (FloatingActionButton) v.findViewById(R.id.btn_save);


        lv_produtos = (ListView) v.findViewById(R.id.lv_lista_compras);

        produtos = new ArrayList<Produto>();
        produtos_db = new ArrayList<Produto>();

        adpProduto = new AdpProduto(getContext(), R.layout.listview_produto, produtos);
        lv_produtos.setAdapter(adpProduto);
        registerForContextMenu(lv_produtos);

        banco = new Banco(this.getContext());

        SQLiteDatabase db = banco.getReadableDatabase();
        if (db == null)
            Toast.makeText(getContext(),"Erro ao carregar o banco de dados", Toast.LENGTH_SHORT).show();


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_produto.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Preencha o campo produto corretamente", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_qtde.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "A quantidade não pode ser vazia", Toast.LENGTH_SHORT).show();
                    return;
                }

                p = new Produto();
                p.setProduto(et_produto.getText().toString());
                p.setQuantidade(Integer.parseInt(et_qtde.getText().toString()));
                produtos.add(p);
                produtos_db.add(p);
                adpProduto.notifyDataSetChanged();
                et_produto.setText("");
                et_qtde.setText("");

            }
        });

        btn_save_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = banco.getWritableDatabase();

                if (lc.getId() == 0){
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String dataFormatada = df.format(date);


                    lc.setData_registro(dataFormatada);
                    lc.setNomeLista(et_nome_lista_compras.getText().toString());

                    ContentValues lista_compra = new ContentValues();

                    lista_compra.put(EsquemaBanco.EsquemaLista.coluna_nome, lc.getNomeLista());
                    lista_compra.put(EsquemaBanco.EsquemaLista.conluna_data, lc.getData_registro());
                    long newRowId = db.insert(EsquemaBanco.EsquemaLista.table_lista, "", lista_compra);

                   lc.setId(newRowId);
//                   Toast.makeText(getContext(), "id da lista: "+ lc.getId(), Toast.LENGTH_SHORT).show();
                }


                ContentValues produto = new ContentValues();

                for (int i = 0; i < produtos_db.size(); i++){
                    produto.put(EsquemaBanco.EsquemaProduto.coluna_descricao, produtos_db.get(i).getProduto());
                    produto.put(EsquemaBanco.EsquemaProduto.conluna_qtde, produtos_db.get(i).getQuantidade());
                    produto.put(EsquemaBanco.EsquemaProduto.coluna_list_id, lc.getId());
                    long newRowProduto = db.insert(EsquemaBanco.EsquemaProduto.table_produto, null, produto);
                }
                produtos_db.clear();

                Cursor read = db.rawQuery("select p.descricao, p.quantidade from Produto as p inner join ListaCompras as lc on p.list_id = lc._id and p.list_id = " + lc.getId(), null);

                read.moveToFirst();
                produtos.clear();
                while (read.isAfterLast() == false){
                    Produto p_list = new Produto();

                    String item = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaProduto.coluna_descricao));
                    String qtde = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaProduto.conluna_qtde));
                    p_list.setProduto(item);
                    p_list.setQuantidade(Integer.parseInt(qtde));
                    produtos.add(p_list);

                    read.moveToNext();
                }

                adpProduto.notifyDataSetChanged();
                et_nome_lista_compras.setText("");
                et_produto.setText("");
                et_produto.setText("");

                Toast.makeText(getContext(), "Lista cadastrada com sucesso", Toast.LENGTH_LONG).show();

            }
        });

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater =  getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contexto, menu);
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
                        produtos.remove(info.position);
                        adpProduto.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Produto removido da lista com sucesso", Toast.LENGTH_SHORT).show();
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
