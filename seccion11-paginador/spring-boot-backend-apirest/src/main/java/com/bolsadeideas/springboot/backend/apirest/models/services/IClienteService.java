package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();

	//Pageable contiene o se crea una instancia de new PageRequest(1,20)
	//calculando de forma automatica el LIMITy el OFFSET. No debemos preocuparnos de eso
	//JpaRepository es una subinterface de PaginAndSortingReposirory
	//Page es parecido al List pero a traves de rangos
	//Page tiene parametros como numero de pag acual, cantidad de elementos que se usa en la vista
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente);
	
	public void delete(Long id);

}
