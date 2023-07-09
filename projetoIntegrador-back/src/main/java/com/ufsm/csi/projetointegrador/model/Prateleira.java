package com.ufsm.csi.projetointegrador.model;

import java.util.ArrayList;

public class Prateleira {

  private int id;

  private ArrayList<Livro> livros;

  public ArrayList<Livro> getLivros() {
    return livros;
  }

  public void setLivros(ArrayList<Livro> livros) {
    this.livros = livros;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
