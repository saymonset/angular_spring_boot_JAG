package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
//dao o repository es lo mismo
//Sring tienen  un componente llamada CrudRepository, tiene los elementos basicos de un crud Esta el JpaRepoditory que hereda de crudRepository
//https://spring.io/projects/spring-data-ldap
public interface IClienteDao extends CrudRepository<Cliente, Long>{

}
