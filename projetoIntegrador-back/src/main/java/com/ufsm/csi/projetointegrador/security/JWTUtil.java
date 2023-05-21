package com.ufsm.csi.projetointegrador.security;

import com.ufsm.csi.projetointegrador.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    public static final long TEMPO_VIDA = Duration.ofDays(120).toMillis();

    public String geraToken(Usuario usuario){

        final Map<String, Object> claims = new HashMap<>();
        claims.put("sub", usuario.getEmail());
        claims.put("permissoes", usuario.getPermissao());

        return Jwts.builder().
                setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+this.TEMPO_VIDA))
                .signWith(SignatureAlgorithm.HS256, "projetoIntegrador")
                .compact();
    }

    public String getUsernameToken(String token){
        if (token != null){
            return this.parseToken(token).getSubject();
        }
        else {
            return null;
        }
    }

    public boolean isTokenExpirado(String token){
        return this.parseToken(token).getExpiration().before(new Date());
    }

    private Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey("projetoIntegrador")
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody();
    }
}
