package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.omg.PortableServer.POA;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*Este es el servidor de recursos : ResourceServerConfigurerAdapter*/
/*Se encargar de dar accesos a los clientes de los recursos de la aplicacion siempre y cuando el token
que se envia por las cabeceras sea un token valido*/
@Configuration
/*Para habilitar esta autorizacion server de recursos, anotamos con esta anotacion*/
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	/*configuracion por lado de oauth*/
	public void configure(HttpSecurity http) throws Exception {
		// todas las reglas para autorizacion
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**", "/api/uploads/img/**", "/images/**").permitAll()

				/*Todas estas reglas de roles, la llevamos a anotaciones*/
				/*Personalizando los permisos para ciertas paginas.
		   Puedes en los roles colocar USER porque automaticamente es insertado el ROLE_  */
			/*hasAnyRole es para agregar mas de un role
		hasRole es para un solo role*/
		/*.antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes/upload").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN")
		para todas las rutas /api/clientes  se necesita role admin
		.antMatchers("/api/clientes/**").hasRole("ADMIN")*/
		/*Pars el resto de las rutas es con usuario autenticado sin importar el role*/
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	
}
