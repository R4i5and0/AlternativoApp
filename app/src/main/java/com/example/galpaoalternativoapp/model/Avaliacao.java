// Arquivo: model/Avaliacao.java
package com.example.galpaoalternativoapp.model;

public class Avaliacao {
    private int id;
    private int usuarioId;
    private String nomeUsuario;
    private String tipo;
    private float nota;
    private String comentario;

    public Avaliacao(int id, int usuarioId, String nomeUsuario, String tipo, float nota, String comentario) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.tipo = tipo;
        this.nota = nota;
        this.comentario = comentario;
    }

    // Getters
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getTipo() { return tipo; }
    public float getNota() { return nota; }
    public String getComentario() { return comentario; }
}