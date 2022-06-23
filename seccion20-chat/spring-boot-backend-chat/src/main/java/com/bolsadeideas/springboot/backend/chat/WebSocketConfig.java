package com.bolsadeideas.springboot.backend.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*ES una clase de configuracion de spring*/
@Configuration
/*habilita el servidor o broker de spring*/
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
			/*agregamos la ruta, cualquiera, en este caso /chat-websocket*/
			registry.addEndpoint("/chat-websocket")
		.setAllowedOrigins("http://localhost:4200")
/*
					usando la libreria de websocket withSockJS por sobre stomp.Por debajo stomp
				va usar withSockJS
		 Nos permite conectarnos con el protocolo http para conectar al servidor de websocket (Broker).
		 De lo contrario, tendriamos que conectanos con la api nativa html5, el api de websocket que viene
		 por defecto en los navegadores pero solo soporta el protocolo ws y no el http.
		 El http es mas compatible ycorremos menos ruesgo que nos bloquee el firewall de las redes corporativas
*/
		.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//colocamos lo que queramos, es el nombre de evento, cuando el servidor emite un mensaje o notifica a
		//todos los clientes, tenemos que indicar el nombre del evento para que los clientes esten subscritos a
		//ese evento
		//habilitamos el prefijo de ese evento o canal /chat/   o cualquer nombre
		//Prefijo para el nombre de os eventos
		registry.enableSimpleBroker("/chat/");
		//Prefijo de la destinacion: Cuando publicamos un mensaje vamos a enviar nuestro payload, nuestro objeto
		//Prefijo del broker hacia un detino que vamos a publicar, ese destino se configura con message mapping
		registry.setApplicationDestinationPrefixes("/app");
	}

}
