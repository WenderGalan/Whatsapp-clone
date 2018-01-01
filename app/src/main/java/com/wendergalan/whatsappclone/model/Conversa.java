package com.wendergalan.whatsappclone.model;

/**
 * Created by Wender Galan Gamer on 01/01/2018.
 */

public class Conversa {
    private String idUsuario;
    private String nome;
    private String mensagem;

    public Conversa() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
