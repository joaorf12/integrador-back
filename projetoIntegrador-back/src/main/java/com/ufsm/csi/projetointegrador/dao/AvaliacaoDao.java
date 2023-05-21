package com.ufsm.csi.projetointegrador.dao;

import com.ufsm.csi.projetointegrador.model.Avaliacao;
import com.ufsm.csi.projetointegrador.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class AvaliacaoDao {
    private String sql;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Statement stmt;
    private String status;

    public String setAvaliacao(Avaliacao avaliacao){
        try (Connection connection = new ConectaDBPostgres().getConexao()) {
            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            this.sql = "INSERT INTO avaliacao (id_livro, id_pessoa, numeracao, dt_hr) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, avaliacao.getId_livro());
            preparedStatement.setInt(2, avaliacao.getId_pessoa());
            preparedStatement.setInt(3, avaliacao.getNumeracao());
            preparedStatement.setDate(4, startDate);
            preparedStatement.execute();
            this.status = "Sucesso!";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public String editarAvaliacao(Avaliacao avaliacao) {
        try (Connection connection = new ConectaDBPostgres().getConexao()) {
            Calendar calendar = Calendar.getInstance();
            java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

            this.sql = "UPDATE avaliacao SET numeracao=?, dt_hr=?WHERE id_pessoa= ? and id_livro= ?";
            this.preparedStatement = connection.prepareStatement(sql);
            this.preparedStatement.setInt(1, avaliacao.getNumeracao());
            this.preparedStatement.setDate(2, startDate);
            this.preparedStatement.setInt(3, avaliacao.getId_pessoa());
            this.preparedStatement.setInt(4, avaliacao.getId_livro());
            this.preparedStatement.executeUpdate();
            if (this.preparedStatement.getUpdateCount() > 0) {
                this.status = "Editado com Sucesso";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.status;
    }

    public ArrayList<Avaliacao> getAvaliacoes(int id){
        ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
        try(Connection connection = new ConectaDBPostgres().getConexao()){
            this.sql = "SELECT * FROM avaliacao where id_livro = ?;";
            this.stmt = connection.createStatement();
            this.preparedStatement.setInt(1, id);
            this.resultSet = this.stmt.executeQuery(sql);

            while(this.resultSet.next()){
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setId_livro(this.resultSet.getInt("id_livro"));
                avaliacao.setId_pessoa(this.resultSet.getInt("id_pessoa"));
                avaliacao.setNumeracao(this.resultSet.getInt("numeracao"));
                avaliacao.setDt_hr(this.resultSet.getDate("dt_hr"));

                avaliacoes.add(avaliacao);
            }

        }catch (SQLException e){
            e.printStackTrace();
            this.status= "error";
        }
        return avaliacoes;
    }

}
