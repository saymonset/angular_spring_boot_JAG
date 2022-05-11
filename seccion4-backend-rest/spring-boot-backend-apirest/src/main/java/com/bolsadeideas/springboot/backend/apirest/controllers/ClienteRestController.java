package com.bolsadeideas.springboot.backend.apirest.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

//Compartir dos plaicacioned que estan en diferentes dominios
//Cross signfica intercambio de recursos de origen cruzado.
//Permite a los navegadores modernos enviar o solicitar datos restringidos(flujos de datos, stream, archivos de un dominio, imagenes )
@CrossOrigin(origins = { "http://localhost:4200" })
//mapeado con spring decorator para usqar rest
@RestController
@RequestMapping("/api")
public class ClienteRestController {


	@Autowired
	//En spring cuando se declara un bean con su tipo generico ya sea una interface o clase abstrata, va a buscar un candidato o clase concreta
	//que implemente esta interface.
	//Si tiene mas de una clase concreta que implementa esta interface, habria que usar un qualificador
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

}
