package com.ufsm.csi.projetointegrador.service;

import com.ufsm.csi.projetointegrador.dao.UsuarioDao;
import com.ufsm.csi.projetointegrador.model.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceCustomizado implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario= new UsuarioDao().login(username);
        if (usuario == null){
            throw new UsernameNotFoundException("Usuário ou senha incorretos!");
        } else if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário desativado!");
        } else {
            return User.withUsername(usuario.getEmail()).password(usuario.getSenha()).authorities(usuario.getPermissao().getNome()).build();
        }
    }
}
