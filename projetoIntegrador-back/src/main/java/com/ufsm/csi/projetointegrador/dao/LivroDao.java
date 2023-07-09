package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.Livro;
import com.ufsm.csi.projetointegrador.model.Usuario;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class LivroDao {
  private String sql;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  private Statement stmt;
  private String status;

  private static final String caminhoCapa = "C:\\Users\\User\\Desktop\\ProjetoIntegrador2404\\ProjetoIntegrador\\projetoIntegrador-front\\src\\assets\\img\\";
  private static final String caminhoPdf = "C:\\Users\\User\\Desktop\\ProjetoIntegrador2404\\ProjetoIntegrador\\pdfs\\";

  public Livro buscarLivro(int id) {
    Livro livro = null;
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "SELECT * FROM livro WHERE id= ?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, id);
      this.resultSet = this.preparedStatement.executeQuery();
      while (this.resultSet.next()) {
        livro = new Livro();
        livro.setId(id);
        livro.setNome(this.resultSet.getString("nome"));
        livro.setAutor(this.resultSet.getString("autor"));
        livro.setAno_publi(this.resultSet.getInt("ano_publi"));
        livro.setNum_pag(this.resultSet.getInt("num_pag"));
        livro.setGenero(this.resultSet.getString("genero"));
        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario usuario = usuarioDao.getUsuarioById(this.resultSet.getInt("id_pessoa"));
        livro.setUsuario(usuario);
        livro.setDt_ult_atualizacao(this.resultSet.getDate("dt_ult_atualizacao"));
        livro.setNum_download(this.resultSet.getInt("num_download"));
        livro.setCapa(this.resultSet.getString("capa"));
        livro.setPdf(this.resultSet.getString("pdf"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      this.status = "error";
    }
    return livro;
  }

  public Livro excluirLivro(Livro livro) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "DELETE FROM livro WHERE id=?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, livro.getId());
      this.preparedStatement.execute();

      File capa = new File(caminhoCapa+livro.getCapa());
      capa.delete();
      File pdf = new File(caminhoPdf+livro.getPdf());
      pdf.delete();
    } catch (PSQLException e) {
      return null;
    } catch (SQLException e) {
      return null;
    }
    return livro;
  }

  public Livro editarLivro(Livro livro) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "UPDATE livro SET nome=?, autor=?, ano_publi=?, num_pag=?, genero=?, dt_ult_atualizacao= ? WHERE id= ?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setString(1, livro.getNome());
      this.preparedStatement.setString(2, livro.getAutor());
      this.preparedStatement.setInt(3, livro.getAno_publi());
      this.preparedStatement.setInt(4, livro.getNum_pag());
      this.preparedStatement.setString(5, livro.getGenero());
      Calendar calendar = Calendar.getInstance();
      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
      this.preparedStatement.setDate(6, startDate);
      this.preparedStatement.setInt(7, livro.getId());
      this.preparedStatement.executeUpdate();

      if(livro.getOld_pdf() != null || livro.getOld_capa() != null){
        if(livro.getOld_capa() != null) {
          File capa = new File(caminhoCapa+livro.getOld_capa());
          capa.delete();
          livro.setCapa(renomaerArquivo(livro.getCapa(), livro.getId(), true));
        }
        if(livro.getOld_pdf() != null) {
          File pdf = new File(caminhoPdf+livro.getOld_pdf());
          pdf.delete();
          livro.setPdf(renomaerArquivo(livro.getPdf(), livro.getId(), false));
        }

        this.sql = "UPDATE livro SET capa=?, pdf=? WHERE id= ?";
        this.preparedStatement = connection.prepareStatement(sql);
        this.preparedStatement.setString(1, livro.getCapa());
        this.preparedStatement.setString(2, livro.getPdf());
        this.preparedStatement.setInt(3, livro.getId());
        this.preparedStatement.executeUpdate();
      }
      if (this.preparedStatement.getUpdateCount() > 0) {
        return livro;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return livro;
  }

  public Livro setLivro(Livro livro) throws SQLException {
    //Falta salvar o pdf
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "INSERT INTO livro (nome, autor, ano_publi, num_pag, genero, id_pessoa, dt_ult_atualizacao, num_download, capa, pdf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, livro.getNome());
      preparedStatement.setString(2, livro.getAutor());
      preparedStatement.setInt(3, livro.getAno_publi());
      preparedStatement.setInt(4, livro.getNum_pag());
      preparedStatement.setString(5, livro.getGenero());
      preparedStatement.setInt(6, livro.getUsuario().getId());
      Calendar calendar = Calendar.getInstance();
      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
      preparedStatement.setDate(7, startDate);
      preparedStatement.setInt(8, 0);
      preparedStatement.setString(9, livro.getCapa());
      preparedStatement.setString(10, livro.getPdf());
      preparedStatement.execute();

      this.sql = "SELECT currval('livro_id_seq');";
      this.preparedStatement = connection.prepareStatement(this.sql);
      this.resultSet = this.preparedStatement.executeQuery();
      while (resultSet.next()) {
        int idLivro = resultSet.getInt("currval");
        livro.setId(idLivro);
        livro.setCapa(renomaerArquivo(livro.getCapa(), idLivro, true));
        livro.setPdf(renomaerArquivo(livro.getPdf(), idLivro, false));
      }

      this.sql = "UPDATE livro SET capa=?, pdf=? WHERE id= ?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setString(1, livro.getCapa());
      this.preparedStatement.setString(2, livro.getPdf());
      this.preparedStatement.setInt(3, livro.getId());
      this.preparedStatement.executeUpdate();
      if (this.preparedStatement.getUpdateCount() > 0) {
        System.out.println("Livro cadastrado!");
      }

    }
    return livro;
  }

  public ArrayList<Livro> getLivros(int id_pessoa) {
    ArrayList<Livro> livros = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select * from livro order by id;";
      this.stmt = connection.createStatement();
      this.resultSet = this.stmt.executeQuery(sql);

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

        livros.add(livro);
      }
      for ( Livro livro : livros ){
        this.sql = "select status from prateleira_livro pl, prateleira p where pl.id_livro = ? and pl.id_prateleira = p.id and p.id_pessoa = ?;";
        this.preparedStatement = connection.prepareStatement(sql);
        this.preparedStatement.setInt(1, livro.getId());
        this.preparedStatement.setInt(2, id_pessoa);
        this.resultSet = this.preparedStatement.executeQuery();

        while (this.resultSet.next()) {
          livro.setStatus(this.resultSet.getString("status"));
        }
      }

    } catch (SQLException e) {
      this.status = "error";
    }
    return livros;
  }

  public ArrayList<Livro> getMyLivros(int id) {
    ArrayList<Livro> livros = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select * from livro where id_pessoa = ? order by id;";
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
        livros.add(livro);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      this.status = "error";
    }
    return livros;
  }

  public ArrayList<Livro> getTheyLivros(int id_livroPessoa, int id_pessoa) {
    ArrayList<Livro> livros = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select l.id,\n" +
        "        l.nome,\n" +
        "        l.autor,\n" +
        "        l.ano_publi,\n" +
        "        l.num_pag,\n" +
        "        l.genero,\n" +
        "        l.id_pessoa,\n" +
        "        l.dt_ult_atualizacao,\n" +
        "        l.num_download,\n" +
        "        l.capa,\n" +
        "       \tl.pdf\n" +
        "\t\tfrom livro l\n" +
        "\t\twhere l.id_pessoa = ? order by id;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, id_livroPessoa);
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
        livros.add(livro);
      }

      for ( Livro livro : livros ){
        this.sql = "select status from prateleira_livro pl, prateleira p where pl.id_livro = ? and pl.id_prateleira = p.id and p.id_pessoa = ?;";
        this.preparedStatement = connection.prepareStatement(sql);
        this.preparedStatement.setInt(1, livro.getId());
        this.preparedStatement.setInt(2, id_pessoa);
        this.resultSet = this.preparedStatement.executeQuery();

        while (this.resultSet.next()) {
          livro.setStatus(this.resultSet.getString("status"));
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      this.status = "error";
    }
    return livros;
  }

  public int aumentaDownload (int id) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "update livro set num_download = num_download + 1 where id = ?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, id);
      this.preparedStatement.executeUpdate();

      if (this.preparedStatement.getUpdateCount() > 0) {
        return 1;
      }
    } catch (SQLException e) {
      return 0;
    }
    return 1;
  }

  public String renomaerArquivo(String arquivo, int idLivro, boolean isCapa) {

    String caminho;
    String tipo = "";
    if (isCapa) {
      caminho = caminhoCapa;
    } else {
      caminho = caminhoPdf;
      tipo = "pdf";
    }
    File oldName =
      new File(caminho + arquivo);
    File newName = null;
    if (isCapa) {
      String[] trataTipo = oldName.getName().split("\\.");
      tipo = trataTipo[trataTipo.length - 1];

      newName =
        new File(caminho + idLivro + "." + tipo);
    } else {
      newName =
        new File(caminho + idLivro + "." + tipo);
    }
    if (oldName.renameTo(newName))
      System.out.println("Renamed successfully");
    else
      System.out.println("Error");
    return idLivro + "." + tipo;
  }
}
