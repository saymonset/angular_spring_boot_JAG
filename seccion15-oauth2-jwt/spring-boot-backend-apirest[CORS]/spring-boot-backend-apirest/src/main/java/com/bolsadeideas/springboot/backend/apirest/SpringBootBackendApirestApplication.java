package com.bolsadeideas.springboot.backend.apirest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



		/*#
		Decodificar un JWT minuto 9  de la seccion 15_>122

		tenemos el token
let token =  "token.token.token";
#lo separamos en un arreglo con split y el punto, obtenemos el segundo isicion del arreglo
let payload = token.split(".")[1];
#Imprimimos el payload y lo copiamos
payload;
##Lo decodificamos con javascript base 64
window.atob(payload);
Lo conertimos a un objeto javaScript
JSON.parse(windows.atob(payload));

En resumen los jwt son codificados en doble via, son reversibles

Spring security es n proyecto que se integra con spring framework
*/
@SpringBootApplication
public class SpringBootBackendApirestApplication implements CommandLineRunner{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApirestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String password = "12345";
		/*generamos 4 password para la misma contrasena 12345*/
		for (int i = 0; i < 4; i++) {
			String passwordBcrypt = passwordEncoder.encode(password);
			System.out.println(passwordBcrypt);
		}
		
	}
}
