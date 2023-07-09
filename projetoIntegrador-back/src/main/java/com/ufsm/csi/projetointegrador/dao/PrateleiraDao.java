package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.*;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;

public class PrateleiraDao {

  private String sql;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  private Statement stmt;

  public Prateleira setPrateleira(Usuario usuario) throws SQLException{
    Prateleira prateleira = new Prateleira();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "INSERT INTO prateleira (id_pessoa) VALUES (?)";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, usuario.getId());
      preparedStatement.execute();
    }

    return prateleira;
  }

  public Prateleira getPrateleira(int id) {
    Prateleira prateleira = new Prateleira();
    ArrayList<Livro> livros = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select \n" +
        "l.id,\n" +
        "l.nome,\n" +
        "l.autor,\n" +
        "l.ano_publi,\n" +
        "l.num_pag,\n" +
        "l.genero,\n" +
        "l.id_pessoa,\n" +
        "l.dt_ult_atualizacao,\n" +
        "l.num_download,\n" +
        "l.capa,\n" +
        "l.pdf,\n" +
        "pl.id_prateleira,\n" +
        "pl.status\n" +
        "from livro l, prateleira_livro pl, prateleira p \n" +
        "where l.id = pl.id_livro and pl.id_prateleira = p.id and p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, id);
      this.resultSet = this.preparedStatement.executeQuery();


      while (this.resultSet.next()) {
        Livro livro = new Livro();
        Usuario usuario = new Usuario();
        livro.setUsuario(usuario);
        livro.setId(this.resultSet.getInt("id"));
        livro.setNome(this.resultSet.getString("nome"));
        livro.setAno_publi(this.resultSet.getInt("ano_publi"));
        livro.setNum_pag(this.resultSet.getInt("num_pag"));
        livro.setAutor(this.resultSet.getString("autor"));
        livro.setGenero(this.resultSet.getString("genero"));
        livro.getUsuario().setId(this.resultSet.getInt("id_pessoa"));
        livro.setDt_ult_atualizacao(this.resultSet.getDate("dt_ult_atualizacao"));
        livro.setNum_download(this.resultSet.getInt("num_download"));
        livro.setCapa(this.resultSet.getString("capa"));
        livro.setPdf(this.resultSet.getString("pdf"));
        livro.setStatus(this.resultSet.getString("status"));
        livros.add(livro);
        prateleira.setId(this.resultSet.getInt("id_prateleira"));
      }
      prateleira.setLivros(livros);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return prateleira;
  }

  public Prateleira setLivroInterresse(PrateleiraLivro prateleira_livro) throws SQLException{
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select p.id from prateleira p where \n" +
        "p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_pessoa());
      this.resultSet = this.preparedStatement.executeQuery();

      while (this.resultSet.next()) {
        prateleira_livro.setId_prateleira(this.resultSet.getInt("id"));
        break;
      }

      this.sql = "select \n" +
        "l.id,\n" +
        "pl.status\n" +
        "from livro l, prateleira_livro pl, prateleira p \n" +
        "where l.id = pl.id_livro and pl.id_prateleira = p.id and p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_pessoa());
      this.resultSet = this.preparedStatement.executeQuery();

      boolean temLivro = false;
      String status = "";

      while (this.resultSet.next()) {
        if ( this.resultSet.getInt("id") == prateleira_livro.getId_livro()){
          temLivro = true;
          status = this.resultSet.getString("status");
        }
      }

      if(!temLivro){
        this.sql = "insert into prateleira_livro (id_prateleira, id_livro, status) values (?, ?, 'interesse');";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, prateleira_livro.getId_prateleira());
        preparedStatement.setInt(2, prateleira_livro.getId_livro());
        preparedStatement.execute();
      }
      else if (status.equals("interesse")){
        this.sql = "delete from prateleira_livro where id_livro = ? and id_prateleira = ?";
        this.preparedStatement = connection.prepareStatement(sql);
        this.preparedStatement.setInt(1, prateleira_livro.getId_livro());
        this.preparedStatement.setInt(2, prateleira_livro.getId_prateleira());
        this.preparedStatement.execute();
      }
    }

    return null;
  }

  public Prateleira setLivroLido(PrateleiraLivro prateleira_livro) throws SQLException{
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select p.id from prateleira p where \n" +
        "p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_pessoa());
      this.resultSet = this.preparedStatement.executeQuery();

      while (this.resultSet.next()) {
        prateleira_livro.setId_prateleira(this.resultSet.getInt("id"));
        break;
      }

      this.sql = "select \n" +
        "l.id,\n" +
        "pl.status\n" +
        "from livro l, prateleira_livro pl, prateleira p \n" +
        "where l.id = pl.id_livro and pl.id_prateleira = p.id and p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_pessoa());
      this.resultSet = this.preparedStatement.executeQuery();

      String status = "";

      while (this.resultSet.next()) {
        if ( this.resultSet.getInt("id") == prateleira_livro.getId_livro()){
          status = this.resultSet.getString("status");
        }
      }

      if(status.equals("interesse") || status.equals("lendo")){
        this.sql = "update prateleira_livro set status = 'lido' where id_prateleira = ? and id_livro = ?;";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, prateleira_livro.getId_prateleira());
        preparedStatement.setInt(2, prateleira_livro.getId_livro());
        preparedStatement.execute();
      } else if (!status.equals("lido")) {
        this.sql = "insert into prateleira_livro (id_prateleira, id_livro, status) values (?, ?, 'lido');";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, prateleira_livro.getId_prateleira());
        preparedStatement.setInt(2, prateleira_livro.getId_livro());
        preparedStatement.execute();
      }
    }

    return null;
  }

  public Prateleira setLivroLendo(PrateleiraLivro prateleira_livro) throws SQLException{
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select p.id from prateleira p where \n" +
        "p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_pessoa());
      this.resultSet = this.preparedStatement.executeQuery();

      while (this.resultSet.next()) {
        prateleira_livro.setId_prateleira(this.resultSet.getInt("id"));
        break;
      }

      this.sql = "select \n" +
        "l.id,\n" +
        "pl.status\n" +
        "from livro l, prateleira_livro pl, prateleira p \n" +
        "where l.id = pl.id_livro and pl.id_prateleira = p.id and p.id_pessoa = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_pessoa());
      this.resultSet = this.preparedStatement.executeQuery();

      String status = "";

      while (this.resultSet.next()) {
        if ( this.resultSet.getInt("id") == prateleira_livro.getId_livro()){
          status = this.resultSet.getString("status");
        }
      }

      if(!status.equals("lendo") && !status.equals("")){
        System.out.println("sadasdasd");
        this.sql = "update prateleira_livro set status = 'lendo' where id_prateleira = ? and id_livro = ?;";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, prateleira_livro.getId_prateleira());
        preparedStatement.setInt(2, prateleira_livro.getId_livro());
        preparedStatement.execute();
      }
      else if (!status.equals("lendo")){
        this.sql = "insert into prateleira_livro (id_prateleira, id_livro, status) values (?, ?, 'lendo');";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, prateleira_livro.getId_prateleira());
        preparedStatement.setInt(2, prateleira_livro.getId_livro());
        preparedStatement.execute();
      }
    }

    return null;
  }

  public int deleteLivro(PrateleiraLivro prateleira_livro) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {

      this.sql = "delete from prateleira_livro pl where id_prateleira = ? and id_livro = ?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, prateleira_livro.getId_prateleira());
      this.preparedStatement.setInt(2, prateleira_livro.getId_livro());
      this.preparedStatement.execute();

    } catch (PSQLException e) {
      return 0;
    } catch (SQLException e) {
      return 0;
    }
    return 1;
  }
}
