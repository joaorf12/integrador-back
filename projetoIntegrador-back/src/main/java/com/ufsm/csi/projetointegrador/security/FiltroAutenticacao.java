package com.ufsm.csi.projetointegrador.security;

import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FiltroAutenticacao extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();

        try {
            if(!url.contains("login")){
                String token = request.getHeader("Authorization");
                String username = new JWTUtil().getUsernameToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (! new JWTUtil().isTokenExpirado(token)){
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }

                }
            }
        }catch (ExpiredJwtException e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expirado"+e);
        }

        filterChain.doFilter(request, response);
    }
}
