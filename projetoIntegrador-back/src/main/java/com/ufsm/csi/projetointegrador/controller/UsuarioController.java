package com.ufsm.csi.projetointegrador.controller;

import com.ufsm.csi.projetointegrador.model.Senha;
import com.ufsm.csi.projetointegrador.model.Usuario;
import com.ufsm.csi.projetointegrador.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

  private static String caminhoImagens = "C:\\Users\\User\\Desktop\\ProjetoIntegrador2404\\ProjetoIntegrador\\projetoIntegrador-front\\src\\assets\\img\\";

  @PostMapping("/save")
  public Usuario saveUsuario(@RequestBody Usuario usuario) throws SQLException {
    return new UsuarioService().addUsuario(usuario);
  }

  @PostMapping("/desativar")
  public String deleteUsuario(@RequestBody Usuario usuario) {
    return new UsuarioService().desativarUsuario(usuario);
  }

  @PostMapping("/foto")
  public String imgUsuario(@RequestParam("foto") MultipartFile foto) {
    String caminhoFinal = caminhoImagens + foto.getOriginalFilename();
    try {
      if (!foto.isEmpty()) {
        byte[] bytes = foto.getBytes();
        Path caminho = Paths.get(caminhoFinal);
        Files.write(caminho, bytes);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return foto.getOriginalFilename();
  }

  @PostMapping("/editar")
  public Usuario editarUsuario(@RequestBody Usuario usuario) {
    return new UsuarioService().editarUsuario(usuario);
  }

  @PostMapping("/senha")
  public int editarSenha(@RequestBody Senha senha) {
    return new UsuarioService().editarSenha(senha);
  }

  @GetMapping("/usuarios/{id}")
  public ArrayList<Usuario> getUsuarios(@PathVariable("id") int id) {
    return new UsuarioService().getUsuarios(id);
  }
}
