package com.example.galpaoalternativoapp.model;

import java.io.Serializable;

public class MensagemMural implements Serializable {

    private int id;
    private final int usuarioId;
    private String nomeUsuario;
    private String texto;

    public MensagemMural(int id, int usuarioId, String nomeUsuario, String texto) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.texto = texto;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getTexto() { return texto; }

    public void setId(int id) { this.id = id; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setTexto(String texto) { this.texto = texto; }
}
