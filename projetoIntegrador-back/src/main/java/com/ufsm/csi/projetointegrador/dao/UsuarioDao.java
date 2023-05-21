package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.Permissao;
import com.ufsm.csi.projetointegrador.model.Usuario;
import org.postgresql.util.PSQLException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class UsuarioDao {
  private String sql;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;
  private Statement stmt;
  private String status;

  public String desAtivarUsuario(Usuario usuario) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      boolean ativo;
      if (usuario.isAtivo()) {
        ativo = false;
      } else {
        ativo = true;
      }
      this.sql = "update pessoa\n" +
        "    set ativo = ?\n" +
        "        where id = ?;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setBoolean(1, ativo);
      this.preparedStatement.setInt(2, usuario.getId());
      this.preparedStatement.execute();

    } catch (PSQLException p) {
      return status = "Usuário vinculado a um processo";
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return status = "Usuario desativado!";
  }

  public ArrayList<Usuario> getUsuarios() {
    ArrayList<Usuario> usuarios = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "SELECT pessoa.id, pessoa.id_permissao, pessoa.nome, pessoa.email, pessoa.foto, senha, ativo, p.nome as nome_permissao FROM Pessoa left join permissao p on id_permissao = p.id;";
      this.stmt = connection.createStatement();
      this.resultSet = this.stmt.executeQuery(sql);

      while (this.resultSet.next()) {
        Usuario usuario = new Usuario();
        usuario.setId(this.resultSet.getInt("id"));
        usuario.setNome(this.resultSet.getString("nome"));
        usuario.setEmail(this.resultSet.getString("email"));
        usuario.setSenha(this.resultSet.getString("senha"));
        usuario.setFoto(this.resultSet.getString("foto"));
        usuario.setAtivo(this.resultSet.getBoolean("ativo"));
        Permissao permissao = new Permissao(resultSet.getInt("id_permissao"), resultSet.getString("nome_permissao"));
        usuario.setPermissao(permissao);

        usuarios.add(usuario);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      this.status = "error";
    }
    return usuarios;
  }

  public Usuario getUsuarioByEmail(String email) {
    Usuario usuario = null;

    try (Connection connection = new ConectaDBPostgres().getConexao()) {

      this.sql = "SELECT pessoa.id, pessoa.id_permissao, pessoa.nome, pessoa.email, pessoa.foto, senha, ativo, p.nome as nome_permissao  FROM Pessoa, permissao p WHERE email= ? and pessoa.id_permissao = p.id;";
      this.preparedStatement = connection.prepareStatement(this.sql);
      this.preparedStatement.setString(1, email);
      this.resultSet = this.preparedStatement.executeQuery();
      while (resultSet.next()) {
        usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setFoto(resultSet.getString("foto"));
        usuario.setAtivo(this.resultSet.getBoolean("ativo"));
        Permissao p = new Permissao(resultSet.getInt("id_permissao"), resultSet.getString("nome_permissao"));
        usuario.setPermissao(p);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return usuario;
  }

  public Usuario getUsuarioById(int id) {
    Usuario usuario = null;

    try (Connection connection = new ConectaDBPostgres().getConexao()) {

      this.sql = "SELECT * FROM Pessoa WHERE id= ?";
      this.preparedStatement = connection.prepareStatement(this.sql);
      this.preparedStatement.setInt(1, id);
      this.resultSet = this.preparedStatement.executeQuery();
      while (resultSet.next()) {
        usuario = new Usuario();
        usuario.setId(resultSet.getInt("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setAtivo(this.resultSet.getBoolean("ativo"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return usuario;
  }

  public Usuario setUsuario(Usuario usuario) throws SQLException {

    Calendar calendar = Calendar.getInstance();
    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "INSERT INTO pessoa (nome, email, senha, ativo, id_permissao, foto, dt_hr_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, usuario.getNome());
      preparedStatement.setString(2, usuario.getEmail());
      preparedStatement.setString(3, usuario.getSenha());
      preparedStatement.setBoolean(4, true);
      preparedStatement.setInt(5, 2);
      preparedStatement.setString(6, "icon-usuario.png");
      preparedStatement.setDate(7, startDate);
      preparedStatement.execute();

      this.sql = "SELECT currval('pessoa_id_seq');";
      this.preparedStatement = connection.prepareStatement(this.sql);
      this.resultSet = this.preparedStatement.executeQuery();
      while (resultSet.next()) {
        usuario.setId(resultSet.getInt("currval"));
      }

      new PrateleiraDao().setPrateleira(usuario);
    }
    return usuario;
  }

  public String editarUsuario(Usuario usuario) {
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "UPDATE usuario SET nome=?, email=?, senha=? WHERE id= ?";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setString(1, usuario.getNome());
      this.preparedStatement.setString(2, usuario.getEmail());
      this.preparedStatement.setString(3, usuario.getSenha());
      this.preparedStatement.setInt(4, usuario.getId());
      this.preparedStatement.executeUpdate();
      if (this.preparedStatement.getUpdateCount() > 0) {
        this.status = "Editado com Sucesso";
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return this.status;
  }

  public Usuario login(String user) {
    Usuario usuario = this.getUsuarioByEmail(user);
    if (usuario.getPermissao().getNome().equals("USER")) {
      return new Usuario(user, new BCryptPasswordEncoder().encode(usuario.getSenha()), usuario.getPermissao(), usuario.isAtivo());
    } else if (usuario.getPermissao().getNome().equals("ADMIN")) {
      return new Usuario(user, new BCryptPasswordEncoder().encode(usuario.getSenha()), usuario.getPermissao(), usuario.isAtivo());
    } else {
      return null;
    }
  }
}
