package com.ufsm.csi.projetointegrador.controller;

import com.ufsm.csi.projetointegrador.dao.ComentarioDao;
import com.ufsm.csi.projetointegrador.dao.LivroDao;
import com.ufsm.csi.projetointegrador.model.Comentario;
import com.ufsm.csi.projetointegrador.model.Livro;
import com.ufsm.csi.projetointegrador.service.LivroService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/livro")
public class LivroController {

  private static String caminhoImagens = "C:\\Users\\User\\Desktop\\ProjetoIntegrador2404\\ProjetoIntegrador\\projetoIntegrador-front\\src\\assets\\img\\";
  private static String caminhoPDF = "C:\\Users\\User\\Desktop\\ProjetoIntegrador2404\\ProjetoIntegrador\\pdfs\\";

  @GetMapping("/livros")
  public ArrayList<Livro> getLivros() {
    return new LivroService().getLivros();
  }

  @GetMapping("/livros/{id}")
  public ArrayList<Livro> getMyLivros(@PathVariable("id") int id) {
    return new LivroService().getMyLivros(id);
  }

  @GetMapping("/buscar/{id}")
  public Livro getLivro(@PathVariable("id") int id) {

    return new LivroService().getLivro(id);
  }

  @PostMapping("/save")
  public Livro saveLivro(@RequestBody Livro livro) {
    try {
      return new LivroService().addLivro(livro);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping("/capa")
  public String imgLivro(@RequestParam("capa") MultipartFile capa) {
    String caminhoFinal = caminhoImagens+capa.getOriginalFilename();
    try{
      if(!capa.isEmpty()){
        byte[] bytes = capa.getBytes();
        Path caminho = Paths.get(caminhoFinal);
        Files.write(caminho, bytes);
      }
    } catch (IOException e){
      e.printStackTrace();
    }
    return capa.getOriginalFilename();
  }

  @PostMapping("/pdf")
  public String pdfLivro(@RequestParam("pdf") MultipartFile pdf) {
    String caminhoFinal = caminhoPDF+pdf.getOriginalFilename();
    try{
      if(!pdf.isEmpty()){
        byte[] bytes = pdf.getBytes();
        Path caminho = Paths.get(caminhoFinal);
        Files.write(caminho, bytes);
      }
    } catch (IOException e){
      e.printStackTrace();
    }
    return pdf.getOriginalFilename();
  }

  @GetMapping("/download/{pdf}")
  public HttpEntity<byte[]> downloadLivro(@PathVariable("pdf") String pdf/*@RequestParam String pdf*/) throws IOException {
    byte[] arquivo = Files.readAllBytes( Paths.get(caminhoPDF+pdf) );

    HttpHeaders httpHeaders = new HttpHeaders();

    httpHeaders.add("Content-Disposition", "attachment;filename=\"pdf-livro.pdf\"");

    HttpEntity<byte[]> entity = new HttpEntity<byte[]>( arquivo, httpHeaders);

    return entity;
  }

  @PostMapping("/delete")
  public Livro deleteLivro(@RequestBody Livro livro) {

    return new LivroService().excluiLivro(livro);
  }

  @PostMapping("/editar")
  public Livro editarLivro(@RequestBody Livro livro) {
    return new LivroService().editarLivro(livro);
  }

  //COMENTARIOS
  @PostMapping("/comentario/save")
  public Comentario saveComentario(@RequestBody Comentario comentario) {
    return new ComentarioDao().setComentario(comentario);
  }

  @GetMapping("/comentario/delete/{id}")
  public int deleteComentario(@PathVariable("id") int id) {
    return new ComentarioDao().deleteComentario(id);
  }

  @GetMapping("/comentario/{id}")
  public ArrayList<Comentario> getComentarios(@PathVariable("id") int id_livro) {
    return new ComentarioDao().getComentarios(id_livro);
  }

    /*@GetMapping("/getPdf")
    public StringBuilder getPdf(@PathVariable("livro") Livro livro){
        return new LivroService().getPdf(livro);
    }*/
}
