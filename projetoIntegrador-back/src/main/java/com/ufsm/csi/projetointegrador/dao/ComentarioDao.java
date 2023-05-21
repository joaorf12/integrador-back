package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.Avaliacao;
import com.ufsm.csi.projetointegrador.model.Comentario;
import com.ufsm.csi.projetointegrador.model.Livro;
import com.ufsm.csi.projetointegrador.model.Usuario;
import org.postgresql.util.PSQLException;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ComentarioDao {
    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Statement stmt;
    private String status;

    public Comentario setComentario(Comentario comentario){
        try (Connection connection = new ConectaDBPostgres().getConexao()) {

            this.sql = "INSERT INTO comentario (id_pessoa, id_livro, texto, dt_hr) VALUES (?, ?, ?, current_timestamp)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, comentario.getUsuario().getId());
            preparedStatement.setInt(2, comentario.getLivro().getId());
            preparedStatement.setString(3, comentario.getTexto());
            preparedStatement.execute();
            this.status = "Sucesso!";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comentario;
    }

    public int deleteComentario(int id){
      try (Connection connection = new ConectaDBPostgres().getConexao()) {

        this.sql = "DELETE FROM comentario WHERE id=?";
        this.preparedStatement = connection.prepareStatement(sql);
        this.preparedStatement.setInt(1, id);
        this.preparedStatement.execute();

      } catch (PSQLException e) {
        e.printStackTrace();
        status = "O livro está vinculado a um processo!";
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return 1;
    }

    public ArrayList<Comentario> getComentarios(int id_livro) {
    ArrayList<Comentario> comentarios = new ArrayList<>();
    try (Connection connection = new ConectaDBPostgres().getConexao()) {
      this.sql = "select * from comentario where id_livro = ? order by dt_hr;";
      this.preparedStatement = connection.prepareStatement(sql);
      this.preparedStatement.setInt(1, id_livro);
      this.resultSet = this.preparedStatement.executeQuery();

      while (this.resultSet.next()) {
        Comentario comentario = new Comentario();
        Usuario usuario = new Usuario();
        Livro livro = new Livro();
        comentario.setLivro(livro);
        comentario.setUsuario(usuario);
        comentario.setId(this.resultSet.getInt("id"));
        comentario.getUsuario().setId(this.resultSet.getInt("id_pessoa"));
        comentario.getLivro().setId(this.resultSet.getInt("id_livro"));
        comentario.setTexto(this.resultSet.getString("texto"));
        comentario.setDt_hr(this.resultSet.getTimestamp("dt_hr"));

        comentario.setUsuario(new UsuarioDao().getUsuarioById(comentario.getUsuario().getId()));

        comentarios.add(comentario);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      this.status = "error";
    }
    return comentarios;
  }
}
