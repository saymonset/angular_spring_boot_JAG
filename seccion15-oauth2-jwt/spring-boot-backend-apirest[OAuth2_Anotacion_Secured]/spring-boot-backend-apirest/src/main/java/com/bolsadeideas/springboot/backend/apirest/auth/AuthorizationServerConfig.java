package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	//Para asegurarnos que justo sea ese el objeto , lo llamamos con qualifier y el nombre que es el calificador
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		/*Permisos de los endpoints*/
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		/*Aqui registramos el clente que se va a conectar*/
		clients.inMemory().withClient("angularapp")
				/*cualquer contrasena*/
		.secret(passwordEncoder.encode("12345"))
				/*Permiso que va a tener el cliente*/
		.scopes("read", "write")
			/*	signar el tipo de consecion que va a tener nuestra aplicacion
				Un tipo de autenticacion via pagina de login (password)*/
			/*y otra concesion es a traves de refresh_token que nos permite obtener un token renovado*/
		.authorizedGrantTypes("password", "refresh_token")
				//Tiempo validacion de token
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
	}

	/*Se encarga de todo el proceso de autenticacion y de validar el token*/
	/*cada ves  que iniciamos session, enviaos nuestro suario y password y si todo sale bien , realiza la autenticacion
		y genera el token*/
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));

		/*primer paso para autenticar, generar token y validar token si ya viene, es autenticationManager*/
		endpoints.authenticationManager(authenticationManager)
				//Por debjo tambien trabaja con un token storage, pero por debajo usando el accessTokenConverter
				//pero es opcional
		.tokenStore(tokenStore())
				/*segundo paso es registrar el accessTokenConverter, es el encargado de manejar varias cosas al token, como
				por ejemplo almacena los datos de autenticacion, el payload etc*/
		.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/*segundo paso es registrar el accessTokenConverter, es el encargado de manejar varias cosas al token, como
            por ejemplo almacena los datos de autenticacion, el payload etc*/
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVADA);
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLICA);
		return jwtAccessTokenConverter;
	}
	

}
