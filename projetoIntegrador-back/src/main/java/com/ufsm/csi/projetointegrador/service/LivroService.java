package com.ufsm.csi.projetointegrador.service;

import com.ufsm.csi.projetointegrador.dao.LivroDao;
import com.ufsm.csi.projetointegrador.model.Livro;

import java.sql.SQLException;
import java.util.ArrayList;

public class LivroService {
  private LivroDao dao = new LivroDao();

  public Livro addLivro(Livro livro) throws SQLException {
    return dao.setLivro(livro);
  }

  public ArrayList<Livro> getLivros() {
    return dao.getLivros();
  }

  public ArrayList<Livro> getMyLivros(int id) {
    return dao.getMyLivros(id);
  }

  public Livro getLivro(int id) {
    return dao.buscarLivro(id);
  }

  public Livro excluiLivro(Livro livro) {
    return dao.excluirLivro(livro);
  }

  public Livro editarLivro(Livro livro) {
    return dao.editarLivro(livro);
  }
  //public StringBuilder getPdf(Livro livro){return dao.getPdf(livro);}
}
