package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
//JpaRepository es una subinterface de PaginAndSortingReposirory
//esta JpaRepository hereda de PagingAndSortingRepository
public interface IClienteDao extends JpaRepository<Cliente, Long>{

}
