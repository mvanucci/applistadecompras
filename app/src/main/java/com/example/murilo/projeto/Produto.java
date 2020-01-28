package com.example.murilo.projeto;

/**
 * Created by murilo on 11/13/17.
 */

public class Produto {
    private long id = 0  ;
    private String produto = null;
    private Integer quantidade = null;

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
