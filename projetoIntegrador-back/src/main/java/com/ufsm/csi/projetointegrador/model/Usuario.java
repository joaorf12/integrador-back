package com.ufsm.csi.projetointegrador.model;

public class Usuario {
    private int id;
    private Permissao permissao;
    private String nome;
    private String email;
    private String senha;
    private boolean ativo;
    private String token;
    private String foto;
    private String oldFoto;

  public String getOldFoto() {
    return oldFoto;
  }

  public void setOldFoto(String oldFoto) {
    this.oldFoto = oldFoto;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public Usuario(String email, String senha, Permissao permissao, boolean ativo) {
        this.email = email;
        this.senha = senha;
        this.permissao = permissao;
        this.ativo = ativo;
    }
    public Usuario() {}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
