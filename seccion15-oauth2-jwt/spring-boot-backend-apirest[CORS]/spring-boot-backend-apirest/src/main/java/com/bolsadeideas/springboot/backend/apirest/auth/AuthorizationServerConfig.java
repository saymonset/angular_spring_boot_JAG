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

/*AuthorizationServerConfigurerAdapter: Se encarga de todo el proceso de autentication del lado de oauth2.
Todo lo que tiene que ver con el token jwt desde el proceso de login, crear el token, validarlo*/
@Configuration
/*Para habilitar esta autorizacion server, anotamos con esta anotacion*/
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

	/*Utlizando este metodo que se marco con bean en otra clase de configuracion*/
	/*Creamos este metodo a un componente en spring con @bean para poderlo injectarlo  mas adelante*/
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/*Aqui se llama el authenticationManager*/
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;

	@Override

	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		/*le damos permiso a cualquier usuario anonimo o no para autenticarse*/
		//crea el token
		security.tokenKeyAccess("permitAll()")
				// este es para chequear el token
				//EndPoint que verifica el token y su firma: "/oauth/check_token"
				//Solo accede a esta ruta los clientes autenticados
		.checkTokenAccess("isAuthenticated()");
	}

	/*aqui configuramos nuestro cliente, nuestra aplicaciones que van a acceder al api rest*/
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		/*registramos la aplicacion que se va a conectar con nuestro backend*/
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345"))
				//Los permisos que va a tener la aplicacion
		.scopes("read", "write")
				//muy importante , es el tipo de concesion para poder acceder a traves de password, o refresh_token
				//Aqui es como vas obtener el token, en nuestro caso es con passowrd y refresh_token
				//refresh_token nos permite obtener un token renovado antes que caduque el token
		.authorizedGrantTypes("password", "refresh_token")
				//tiempo caducidad, una hora es 3600 seg
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
	}

	/*se encarga de todo el proceso de autenticacion y de validar el token*/
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		/*primer paso es authenticationManager*/
		endpoints.authenticationManager(authenticationManager)
				//segundo paso es registrar el accessTokenConverter.
				// accessTokenConverter: Encargado de almacenar los datos de autenticacion
				//tokenStore(tokenStore() es opcional
		.tokenStore(tokenStore())
				//segundo paso es registrar el accessTokenConverter.
				// accessTokenConverter: Encargado de almacenar los datos de autenticacion
		.accessTokenConverter(accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVADA);
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLICA);
		return jwtAccessTokenConverter;
	}
	

}
