package com.ufsm.csi.projetointegrador.model;

import java.sql.Date;

public class Avaliacao {
    private int id_pessoa;
    private int id_livro;
    private int numeracao;
    private Date dt_hr;

    public int getId_pessoa() {
        return id_pessoa;
    }

    public void setId_pessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public int getId_livro() {
        return id_livro;
    }

    public void setId_livro(int id_livro) {
        this.id_livro = id_livro;
    }

    public int getNumeracao() {
        return numeracao;
    }

    public void setNumeracao(int numeracao) {
        this.numeracao = numeracao;
    }

    public Date getDt_hr() {
        return dt_hr;
    }

    public void setDt_hr(Date dt_hr) {
        this.dt_hr = dt_hr;
    }
}
