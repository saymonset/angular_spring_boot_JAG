package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	//la url o path de post es el mismo que el get maping, lo diferencia que es get o post
	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	//@PathVariable  usamos ya que estamos pasando el id por url y es el mismo id
	@GetMapping("/clientes/{id}")
	//Es redundante colocar esta anotacion @ResponseStatus(HttpStatus.OK), porque por defecto es asignado en 0k que es 200
	public Cliente show(@PathVariable Long id) {
		return this.clienteService.findById(id);
	}
	//la url o path de post es el mismo que el get maping, lo diferencia que es get o post
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	//recibe el objeto cliente. Como viene objeto json dentro del cuerpo del request, entonces le colocamos @RequestBody
	//spring transforma el json que viene y lo mapea a cliente con @RequestBody
	public Cliente create(@RequestBody Cliente cliente) {
		cliente.setCreateAt(new Date());
		this.clienteService.save(cliente);
		return cliente;
	}

	//la url o path de update es el mismo que el getmaping, lo diferencia qe es get o put
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	//recibe el objeto cliente. Como viene objeto json dentro del cuerpo del request, entonces le colocamos @RequestBody
	//spring transforma el json que viene y lo mapea a cliente con @RequestBody
	//recibe el id, le colocams @PathVariable
	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente currentCliente = this.clienteService.findById(id);
		currentCliente.setNombre(cliente.getNombre());
		currentCliente.setApellido(cliente.getApellido());
		currentCliente.setEmail(cliente.getEmail());
		this.clienteService.save(currentCliente);
		return currentCliente;
	}

	//el verbo delete
	@DeleteMapping("/clientes/{id}")
	//como no retorna ningun contenido, usamos NO_CONTENT
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		Cliente currentCliente = this.clienteService.findById(id);
		this.clienteService.delete(currentCliente);
	}
}
