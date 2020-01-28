package com.example.murilo.projeto;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompraList extends Fragment {

    private ListView lv_lista_compras = null;
    private ArrayList<ListaCompras> listaCompras = null;
    private AdpListaCompras adpListaCompras = null;
    private Banco banco;
    private ListaCompras lc;

    private conFrag cf;

    public interface conFrag{
        public void getListaId(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof conFrag){
            cf = (conFrag) context;
        }
        else{
            throw new RuntimeException(context.toString() + "Deve ser implementado o metodo na atividade");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        cf = null;
    }

    public CompraList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_compra_list, container, false);

        lv_lista_compras = v.findViewById(R.id.lv_lista_compras);
        listaCompras = new ArrayList<ListaCompras>();
        adpListaCompras = new AdpListaCompras(getContext(), R.layout.listview_lista_produtos, listaCompras);
        lv_lista_compras.setAdapter(adpListaCompras);
        registerForContextMenu(lv_lista_compras);

        banco = new Banco(this.getContext());

        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor read = db.rawQuery("Select _id, nome, data_registro from "+ EsquemaBanco.EsquemaLista.table_lista, null);

        read.moveToFirst();
        listaCompras.clear();
        while (read.isAfterLast() == false){
            int id = read.getInt(read.getColumnIndex(EsquemaBanco.EsquemaLista._ID));
            String nomeLista = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaLista.coluna_nome));
            String dataCriada = read.getString(read.getColumnIndex(EsquemaBanco.EsquemaLista.conluna_data));

            lc = new ListaCompras();
            lc.setId(id);
            lc.setNomeLista(nomeLista);
            lc.setData_registro(dataCriada);
            listaCompras.add(lc);
            read.moveToNext();
        }

        adpListaCompras.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi = getActivity().getMenuInflater();
        mi.inflate(R.menu.menu_context_lista_compras, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.i_edit:

                if (cf != null){
                    SQLiteDatabase db = banco.getReadableDatabase();
                    Cursor read = db.rawQuery("Select _id from ListaCompras where nome='" + listaCompras.get(info.position).getNomeLista()+ "'", null);
                    read.moveToFirst();

                    do {
                        int id = read.getInt(read.getColumnIndex(EsquemaBanco.EsquemaLista._ID));
                        cf.getListaId(id);
                        lc.setId(id);
                    } while (read.moveToNext());


                }


                return true;
            case R.id.i_dell:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Atenção");
                builder.setMessage("Deseja realmente deletar essa lista de compras ?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SQLiteDatabase db = banco.getWritableDatabase();
                        db.delete(EsquemaBanco.EsquemaProduto.table_produto, "list_id = ?", new String[]{String.valueOf(listaCompras.get(info.position).getId())});
                        db.delete(EsquemaBanco.EsquemaLista.table_lista, "_id = ?", new String[]{String.valueOf((listaCompras.get(info.position).getId()))});


                        listaCompras.remove(info.position);

                        adpListaCompras.notifyDataSetChanged();

                        Toast.makeText(getContext(), "Lista Excluida com sucesso.", Toast.LENGTH_SHORT).show();

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
