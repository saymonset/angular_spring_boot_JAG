package com.bolsadeideas.springboot.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*Registramos la clase de servicio UsuarioService que implementa la interface  UserDetailsService, la reistramos en el
autentication manager para que se pueda realizar el proceso de autenticacion utilizando jpa*/

@EnableGlobalMethodSecurity(securedEnabled=true)
/*Configuracion de spring*/
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService usuarioService;


	/*passwordEncoder: Metodo que se encarga de crear este passwordEncode usando BCryptPassword*/
	/*Registramos en el contenedor de spring este metod con @Bean para poderlo inyectar mas tarde*/

	/*Para evitar un posible error en las ultimas versiones de spring boot 2.6.0 en adelante:*/
	/*@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}*/
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	//Injectamos via argumento el metodo AuthenticationManagerBuilder
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*passwordEncoder: Metodo que se encarga de crear este passwordEncode usando BCryptPassword*/
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	}

	/*Esto se registra para  inyectarlo en AuthorizationServerConfig que viene de WebSecurityConfigurerAdapter*/
	//Para asegurarnos que justo sea ese el objeto , lo llamamos con qualifier y el nombre que es el calificador
	//redundamos porque si no lo colocamos , es el mismo nombre del metodo. Puedes colocar otro nombre y sustituir
	//en qualifier
	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}
