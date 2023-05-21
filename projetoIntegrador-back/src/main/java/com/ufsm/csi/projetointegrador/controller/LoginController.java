package com.ufsm.csi.projetointegrador.controller;

import com.ufsm.csi.projetointegrador.dao.UsuarioDao;
import com.ufsm.csi.projetointegrador.model.Usuario;
import com.ufsm.csi.projetointegrador.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<Object> autenticacao(@RequestBody Usuario usuario){
        try{
            final Authentication autenticado= this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha()));
            Usuario userAux = new UsuarioDao().getUsuarioByEmail(usuario.getEmail());
            usuario.setPermissao(userAux.getPermissao());
            usuario.setId(userAux.getId());
            usuario.setNome(userAux.getNome());
            usuario.setFoto(userAux.getFoto());
            usuario.setAtivo(userAux.isAtivo());

            if (autenticado.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(autenticado);
                String token = new JWTUtil().geraToken(usuario);
                usuario.setToken(token);
                usuario.setSenha("");
                return new ResponseEntity<>(usuario, HttpStatus.OK);
            }


        }catch (Exception e){
            System.out.println("Acesso Negado!");
            return new ResponseEntity<>("Usuário ou senha incorretos!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Usuário ou senha incorretos!", HttpStatus.BAD_REQUEST);
    }
}
