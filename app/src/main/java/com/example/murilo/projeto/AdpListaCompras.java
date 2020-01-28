package com.example.murilo.projeto;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by murilo on 11/23/17.
 */

public class AdpListaCompras extends ArrayAdapter {
    private Context context;
    private ArrayList<ListaCompras> lc;

    public AdpListaCompras(@NonNull Context context, @LayoutRes int resource, ArrayList<ListaCompras> listaCompras) {
        super(context, resource, listaCompras);
        this.context = context;
        this.lc = listaCompras;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh = null;

        if (convertView == null){
            LayoutInflater li = ((Activity) context).getLayoutInflater();
            convertView = li.inflate(R.layout.listview_lista_produtos, parent, false);

            vh = new ViewHolder();
            vh.nomeLista = (TextView) convertView.findViewById(R.id.str_lista_nome_lista);
            vh.dataCriada = (TextView) convertView.findViewById(R.id.str_lista_data);

            convertView.setTag(vh);
        } else{
            vh = (ViewHolder) convertView.getTag();
        }

        ListaCompras lista_compras = lc.get(position);
        vh.nomeLista.setText("Lista: "+ lista_compras.getNomeLista());
        vh.dataCriada.setText("Data de criação: " + lista_compras.getData_registro());


        return convertView;
    }

    private static class ViewHolder{
        TextView nomeLista;
        TextView dataCriada;
    }
}
