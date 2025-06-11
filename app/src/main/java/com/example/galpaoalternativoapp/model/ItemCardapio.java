// Arquivo: ItemCardapio.java - VERSÃO FINAL CORRIGIDA
package com.example.galpaoalternativoapp.model;

import java.io.Serializable;

public class ItemCardapio implements Serializable {

    private int id;
    private final String nome;
    private final String descricao;
    private final double preco;
    private final Categoria categoria;
    private final int imagem;

    public enum Categoria {
        COMIDA, BEBIDA, SOBREMESA
    }

    // Deixamos apenas o construtor completo que inicializa TODAS as variáveis
    public ItemCardapio(int id, String nome, String descricao, double preco, Categoria categoria, int imagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.imagem = imagem;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getImagem() {
        return imagem;
    }

    public void setId(int id) {
        this.id = id;
    }
}