package com.ufsm.csi.projetointegrador.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  public void configureAutenticacao(AuthenticationManagerBuilder builder) throws Exception {
    builder.userDetailsService(this.userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Bean
  public FiltroAutenticacao filtroAutenticacao() throws Exception {
    return new FiltroAutenticacao();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and()
      .csrf().disable()
      //.authenticationProvider(this.authProvider())
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      //.antMatchers(HttpMethod.GET,"/").permitAll()
      .antMatchers(HttpMethod.POST, "/login").permitAll()
      .antMatchers(HttpMethod.POST, "/usuario/save").permitAll()
      //SESSÃO - parte velha
      .antMatchers(HttpMethod.POST, "/sessao/descricao").permitAll()
      .antMatchers(HttpMethod.GET, "/sessao/sessoes").hasAnyAuthority("ADMIN")
      .antMatchers(HttpMethod.GET, "/sessao/usuario").permitAll()
      .antMatchers(HttpMethod.GET, "/sessao/buscar/{id}").hasAnyAuthority("ADMIN")
      .antMatchers(HttpMethod.GET, "/sessao/delete/{id}").hasAnyAuthority("ADMIN")
      .antMatchers(HttpMethod.POST, "/sessao/editar").hasAnyAuthority("ADMIN")
      .antMatchers(HttpMethod.POST, "/sessao/save").hasAnyAuthority("ADMIN")
      //LIVRO
      .antMatchers(HttpMethod.GET, "/livro/livros").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/livro/capa").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/livro/pdf").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/livros/{id}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/livros/myLivros/{id}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/livros/visitar/{id_livroPessoa}/{id_pessoa}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/download/{pdf}/{id_livro}/{id_pessoa}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/download/{pdf}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/buscar/{id}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/livro/delete").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/livro/editar").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/livro/save").hasAnyAuthority("USER", "ADMIN")
      //AVALIAÇÃO
      .antMatchers(HttpMethod.POST, "/livro/avaliacao").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/avaliacao/{id}").hasAnyAuthority("USER", "ADMIN")
      //COMENTARIO
      .antMatchers(HttpMethod.POST, "/livro/comentario/save").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/comentario/{id}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/livro/comentario/delete/{id}").hasAnyAuthority("USER", "ADMIN")
      //PRATELEIRA
      .antMatchers(HttpMethod.GET, "/prateleira/livros/{id}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/prateleira/delete").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/prateleira/interesse").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/prateleira/lido").hasAnyAuthority("USER", "ADMIN")
      //EMPRESTIMO - parte velha
      .antMatchers(HttpMethod.GET, "/emprestimo/emprestimos").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/emprestimo/save").hasAnyAuthority("ADMIN")
      .antMatchers(HttpMethod.GET, "/emprestimo/devolver/{id_emp}/{id_liv}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/emprestimo/emprestimos/{id_user}").hasAnyAuthority("USER")
      //USUARIO
      .antMatchers(HttpMethod.GET, "/usuario/delete/{id}").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/usuario/editar").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/usuario/foto").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/usuario/senha").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.POST, "/usuario/desativar").hasAnyAuthority("USER", "ADMIN")
      .antMatchers(HttpMethod.GET, "/usuario/usuarios/{id}").hasAnyAuthority("USER", "ADMIN")

      .anyRequest()
      .denyAll();

    http.addFilterBefore(this.filtroAutenticacao(), UsernamePasswordAuthenticationFilter.class);
    //.and().formLogin();
  }

  @Bean
  public CorsFilter corsFilter() {
    final var config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");


    final var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }

}
