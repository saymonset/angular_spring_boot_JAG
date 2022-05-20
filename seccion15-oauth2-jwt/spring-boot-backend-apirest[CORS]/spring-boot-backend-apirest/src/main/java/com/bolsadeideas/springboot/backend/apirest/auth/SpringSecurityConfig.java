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




/*UsuarioService: Esta interface debe estar registrada en el autentication manager(AuthenticationManager). Se debe registrar*/
@EnableGlobalMethodSecurity(securedEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


	/*UsuarioService: Esta interface debe estar registrada en el autentication manager. Se debe registrar*/
	@Autowired
	private UserDetailsService usuarioService;



	/*Para evitar un posible error en las ultimas versiones de spring boot 2.6.0 en adelante:

	BeanCurrentlyInCreationException: Error creating bean with name 'springSecurityConfig': Requested bean is currently in creation: Is there an unresolvable circular reference

	En la clase SpringSecurityConfig, tienen que modificar esto por:



	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	por

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	es decir le agregas la palabra static

	Esto es muy importante que lo tengan en cuenta para la clase anterior (si es que utilizan Spring Boot 2.6.0 o superior), agregar el modificador static en el método passwordEncoder(), mas info:

	https://stackoverflow.com/questions/70254555/spring-boot-2-6-0-error-creating-bean-with-name-websecurityconfig/70265714*/

	/*Creamos este metodo a un componente en spring con @bean para poderlo injectarlo  mas adelante*/
	@Bean
	//metodo que se encarga de crear este passwordEncode
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//aqui hay que pasar una instancia de una implementacion de un encriptador de password para dar mayor seguridad.
		//vamos a usar BCryptPasswordEncoder que es la imementacin por defecto de springSecurity y se recomienda
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	}

	/*Aqui esta el authenticationManager*/
	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	/*configuracion por lado de spring*/
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated()
		.and()
				//deshabilitamos csrf:Cross-site para evitar cualquier ataque del tipo cross-site request forgery o falsificacion
				//de peticion en sitios cruzados.Basicamente es parz proteger nuestros formulariosa traves de un token que no tengan
				//ataques pero como estamos trabajand con angular en un bloque separado, no necesitamos esta proteccion
		.csrf().disable()
				//deshabilitamos las sessiones por la parte de springSecurity o spring
				//La dejamos sin estado o staless ya ue vamos a usartoken para autenticar
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}
