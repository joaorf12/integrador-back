package com.ufsm.csi.projetointegrador.controller;

import com.ufsm.csi.projetointegrador.model.Usuario;
import com.ufsm.csi.projetointegrador.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @PostMapping("/save")
    public Usuario saveUsuario(@RequestBody Usuario usuario) throws SQLException {
        return new UsuarioService().addUsuario(usuario);
    }
    @PostMapping("/desativar")
    public String deleteUsuario(@RequestBody Usuario usuario){
        return new UsuarioService().desativarUsuario(usuario);
    }

    @PostMapping("/editar")
    public String editarUsuario(@RequestBody Usuario usuario){
        return new UsuarioService().editarUsuario(usuario);
    }

    @GetMapping("/usuarios")
    public ArrayList<Usuario> getUsuarios(){

        return new UsuarioService().getUsuarios();
    }
}
