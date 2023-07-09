package com.ufsm.csi.projetointegrador.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Date;

public class Livro {
  private int id;
  private String nome;
  private String autor;
  private int ano_publi;
  private int num_pag;
  private String genero;
  private Usuario usuario;
  private Date dt_ult_atualizacao;
  private int num_download;
  private String pdf;
  private String capa;
  private String old_capa;

  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOld_capa() {
    return old_capa;
  }

  public void setOld_capa(String old_capa) {
    this.old_capa = old_capa;
  }

  public String getOld_pdf() {
    return old_pdf;
  }

  public void setOld_pdf(String old_pdf) {
    this.old_pdf = old_pdf;
  }

  private String old_pdf;


  public String getCapa() {
    return capa;
  }

  public void setCapa(String capa) {
    this.capa = capa;
  }

  public String getPdf() {
    return pdf;
  }

  public void setPdf(String pdf) {
    this.pdf = pdf;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Date getDt_ult_atualizacao() {
    return dt_ult_atualizacao;
  }

  public void setDt_ult_atualizacao(Date dt_ult_atualizacao) {
    this.dt_ult_atualizacao = dt_ult_atualizacao;
  }

  public int getNum_download() {
    return num_download;
  }

  public void setNum_download(int num_download) {
    this.num_download = num_download;
  }

  public int getAno_publi() {
    return ano_publi;
  }

  public void setAno_publi(int ano_publi) {
    this.ano_publi = ano_publi;
  }

  public int getNum_pag() {
    return num_pag;
  }

  public void setNum_pag(int num_pag) {
    this.num_pag = num_pag;
  }

  public String getGenero() {
    return genero;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

}
