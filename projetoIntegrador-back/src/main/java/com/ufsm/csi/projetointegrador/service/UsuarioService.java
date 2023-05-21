package com.ufsm.csi.projetointegrador.service;

import com.ufsm.csi.projetointegrador.dao.LivroDao;
import com.ufsm.csi.projetointegrador.dao.UsuarioDao;
import com.ufsm.csi.projetointegrador.model.Usuario;

import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioService {
    private UsuarioDao dao= new UsuarioDao();
    public Usuario autenticacao(String email, String senha)  {
        if(email!="" && senha!=""){
            Usuario usuario = new UsuarioDao().getUsuarioByEmail(email);
            if( usuario != null){
                if(usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)){
                    return  usuario;
                }
            }else{
                return usuario;
            }
        }
        return null;
    }
    public Usuario addUsuario(Usuario usuario) throws SQLException {
        return dao.setUsuario(usuario);
    }
    public ArrayList<Usuario> getUsuarios(){
        return dao.getUsuarios();
    }
    public Usuario getUsuario(String email){
        return dao.getUsuarioByEmail(email);
    }
    public String desativarUsuario(Usuario usuario){
        return dao.desAtivarUsuario(usuario);
    }
    public String editarUsuario(Usuario Usuario){
        return dao.editarUsuario(Usuario);
    }
}
