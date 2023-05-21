package com.ufsm.csi.projetointegrador.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Comentario {
    private int id;
    private Usuario usuario;
    private Livro livro;
    private String texto;
    private Timestamp dt_hr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Timestamp getDt_hr() {
        return dt_hr;
    }

    public void setDt_hr(Timestamp dt_hr) {
        this.dt_hr = dt_hr;
    }
}
