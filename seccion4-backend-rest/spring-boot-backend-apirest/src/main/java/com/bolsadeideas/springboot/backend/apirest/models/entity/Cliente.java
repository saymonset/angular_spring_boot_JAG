package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
// Esta clase como estamos trabajando con spring y formulario, la podemos guardar de los atributos de la session de forma serializada
@Entity
//Los nombres de tablas en plural
@Table(name="clientes")

public class Cliente implements Serializable {

	@Id
	//estrategia de generacion : auto ,sequence,  identity..
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String apellido;
	private String email;
	
	@Column(name="create_at")
	//DATE SOLO FECHA, TIME SOLO HORA, TIMESTAMP FECHA Y HORA
	//TemporalType convierte el java.date de java.sql de mnera interna
	@Temporal(TemporalType.DATE)
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	//Es requerido cuando se implementa el serializable
	private static final long serialVersionUID = 1L;
}
