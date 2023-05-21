package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.Permissao;
import com.ufsm.csi.projetointegrador.model.Prateleira;
import com.ufsm.csi.projetointegrador.model.Usuario;

import java.sql.*;

public class PrateleiraDao {

  private String sql;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  private Statement stmt;

  public Prateleira setPrateleira(Usuario usuario) throws SQLException{
    Prateleira prateleira = new Prateleira();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "INSERT INTO prateleira VALUES (default)";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.execute();

      this.sql = "SELECT currval('prateleira_id_seq');";
      this.preparedStatement = connection.prepareStatement(this.sql);
      this.resultSet = this.preparedStatement.executeQuery();
      while (resultSet.next()){
        prateleira.setId(resultSet.getInt("currval"));
      }

      this.sql = "INSERT INTO prateleira_pessoa (id_prateleira, id_pessoa) values (?, ?);";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, prateleira.getId());
      preparedStatement.setInt(2, usuario.getId());
      preparedStatement.execute();
    }

    return prateleira;
  }
}
