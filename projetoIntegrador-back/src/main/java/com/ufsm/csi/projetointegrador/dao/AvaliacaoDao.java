package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.Avaliacao;
import com.ufsm.csi.projetointegrador.model.ObjAvaliacao;
import com.ufsm.csi.projetointegrador.model.Usuario;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AvaliacaoDao {
  private String sql;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  private Statement stmt;
  private String status;

  public int setAvaliacao(ObjAvaliacao avaliacao) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "SELECT count(*) FROM avaliacao where id_livro = ? and id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, avaliacao.getId_livro());
      this.preparedStatement.setInt(2, avaliacao.getId_usuario());
      this.resultSet = this.preparedStatement.executeQuery();

      int temAvalicao = 0;
      while (this.resultSet.next()) {
        temAvalicao = this.resultSet.getInt("count");
      }

      if (temAvalicao == 0) {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        this.sql = "INSERT INTO avaliacao (id_livro, id_pessoa, numeracao, dt_hr) VALUES (?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, avaliacao.getId_livro());
        preparedStatement.setInt(2, avaliacao.getId_usuario());
        preparedStatement.setInt(3, avaliacao.getHate());
        preparedStatement.setDate(4, startDate);
        preparedStatement.execute();
        return 1;
      } else {
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        this.sql = "UPDATE avaliacao SET numeracao=?, dt_hr=? WHERE id_pessoa= ? and id_livro= ?";
        this.preparedStatement = connection.prepareStatement(sql);
        this.preparedStatement.setInt(1, avaliacao.getHate());
        this.preparedStatement.setDate(2, startDate);
        this.preparedStatement.setInt(3, avaliacao.getId_usuario());
        this.preparedStatement.setInt(4, avaliacao.getId_livro());
        this.preparedStatement.executeUpdate();
        if (this.preparedStatement.getUpdateCount() > 0) {
          return 1;
        }
      }
    } catch (SQLException e) {
      return 0;
    }
    return 1;
  }

  public ArrayList<ObjAvaliacao> getAvaliacoes(int id) {
    ArrayList<ObjAvaliacao> avaliacoes = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "SELECT * FROM avaliacao where id_livro = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, id);
      this.resultSet = this.preparedStatement.executeQuery();

      while (this.resultSet.next()) {
        ObjAvaliacao avaliacao = new ObjAvaliacao();
        avaliacao.setId_livro(this.resultSet.getInt("id_livro"));
        avaliacao.setId_usuario(this.resultSet.getInt("id_pessoa"));
        avaliacao.setHate(this.resultSet.getInt("numeracao"));
        avaliacao.setData_hr(this.resultSet.getDate("dt_hr"));

        avaliacoes.add(avaliacao);
      }
      return avaliacoes;
    } catch (SQLException e) {
      e.printStackTrace();
      this.status = "error";
    }
    return null;
  }

}
