// Arquivo: model/Evento.java - VERSÃO ATUALIZADA
package com.example.galpaoalternativoapp.model;

import java.io.Serializable;

public class Evento implements Serializable {
    private final String nome;
    private final String descricao;
    private final String data;
    private final int imagem; // <-- ADICIONADO: para guardar a imagem do evento

    // Construtor atualizado para receber a imagem
    public Evento(String nome, String descricao, String data, int imagem) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.imagem = imagem; // <-- ADICIONADO
    }

    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getData() { return data; }
    public int getImagem() { return imagem; } // <-- ADICIONADO
}