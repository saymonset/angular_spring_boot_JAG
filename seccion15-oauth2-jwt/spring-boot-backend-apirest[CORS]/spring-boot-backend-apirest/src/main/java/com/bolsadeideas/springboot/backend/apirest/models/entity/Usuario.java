package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "usuarios")
/*Buena practica es que toda clase entity implement la interface serializable.
* De sta forma se puede serializar convertir de un objeto java a una estructura json y nos
* permite el objeto guardar en una session http
* */
public class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = 20)
	private String username;

	@Column(length = 60)
	private String password;

	private Boolean enabled;
	
	private String nombre;
	private String apellido;

	@Column(unique = true)
	private String email;

	// CascadeType.ALL= cada ves que se elimine al usuario se elimina sus roles.
	//cada ves ue se agrega un usuario, se sgrega sus roles
	//Cuando usamos un manyToMany, se creara una tabla intermedia que va a unr el role con el usuario por sus ids
	//Que los roles y usuarios id en la tabla intermedia sea unica : uniqueConstraints= {@UniqueConstraint(columnNames= {"usuario_id", "role_id"})})

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	//aqui indicamos el nombre de la tabla intermedia si queremos personalizar su nombre. @JoinTable(name="usuarios_rolesSaymosRole"
	@JoinTable(name="usuarios_roles", joinColumns= @JoinColumn(name="usuario_id"),
	inverseJoinColumns=@JoinColumn(name="role_id"),
	uniqueConstraints= {@UniqueConstraint(columnNames= {"usuario_id", "role_id"})})
	private List<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
