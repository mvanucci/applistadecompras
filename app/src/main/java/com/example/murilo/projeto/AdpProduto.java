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
 * Created by murilo on 11/13/17.
 */

public class AdpProduto extends ArrayAdapter<Produto> {

    private Context c;
    private ArrayList<Produto> produtos;

    public AdpProduto(@NonNull Context context, @LayoutRes int resource, ArrayList<Produto> p) {
        super(context, resource, p);
        this.c = context;
        this.produtos = p;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null){
            LayoutInflater li = ((Activity) c).getLayoutInflater();
            convertView = li.inflate(R.layout.listview_produto, parent, false);

            vh = new ViewHolder();
            vh.produto = (TextView) convertView.findViewById(R.id.str_produto);
            vh.quantidade = (TextView) convertView.findViewById(R.id.str_qtde);

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Produto p = produtos.get(position);
        vh.produto.setText("Produto: " + p.getProduto());
        vh.quantidade.setText("Quantidade: " + p.getQuantidade());

        return convertView;
    }

    private static class ViewHolder{
        TextView produto;
        TextView quantidade;
    }
}
