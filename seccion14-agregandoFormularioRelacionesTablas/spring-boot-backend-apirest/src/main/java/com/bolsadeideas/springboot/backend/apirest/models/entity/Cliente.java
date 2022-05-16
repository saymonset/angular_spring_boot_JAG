package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message ="no puede estar vacio")
	@Size(min=4, max=12, message="el tamaño tiene que estar entre 4 y 12")
	@Column(nullable=false)
	private String nombre;
	
	@NotEmpty(message ="no puede estar vacio")
	private String apellido;
	
	@NotEmpty(message ="no puede estar vacio")
	@Email(message="no es una dirección de correo bien formada")
	@Column(nullable=false, unique=true)
	private String email;

	@NotNull(message ="no puede estar vacio")
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	private String foto;

	@NotNull(message="la región no puede ser vacia")
	/*carga perezosa*/
	/*Cuando se llame el metodo getRegion, hay recien , se realiza la carga*/
	@ManyToOne(fetch=FetchType.LAZY)
	/*Aqui indicamos cual va hacr nuestra llave foranea. El campo de la tabla cliente que mapeara a regiones*/
	/*Si se omite, va a colocar de forma automatica este mismo JoinColumn tomando el nombre del atributo region y sumandole un
    sufijo id. Quedaria en region_id. Si quieres cambiar ese nombre, creas el@JoinColumn("NameCampoForaneo")*/
	@JoinColumn(name="region_id")
/*	Con lazy, se genera un proxy al objeto region entity, Este proxy va a generar otros atributos adicionales que
	son propios del framework, entonces, esos atributos que genera de esa clase proxy de region lo  debemos quitar,
	omitir del json con @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}).
  Esta anotacion no tienen nada que ver con entity, solo vamos a omitir estos atributos en la generacion del json.
  ES un arreglo y se colocan dos atribtos.
  hibernateLazyInitializer, handler

	*/
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Region region;
	
	
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

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	private static final long serialVersionUID = 1L;
}
