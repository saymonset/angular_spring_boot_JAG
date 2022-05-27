package com.bolsadeideas.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{
	/*Manual : A traves del nombre del metodo o atraves de consulta personalizada usando like*/
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);
	/*Por spring-data: traves del nombre del metodo o atraves de consulta personalizada usando like*/
	public List<Producto> findByNombreContainingIgnoreCase(String term);

	/*Por spring-data: buscando por nombre que contega esta palabra ignorando que sea mayuscula o minuscula
	* a traves de metodo por spring-data*/
	public List<Producto> findByNombreStartingWithIgnoreCase(String term);
}
