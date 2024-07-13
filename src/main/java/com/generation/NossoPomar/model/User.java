package com.generation.NossoPomar.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuario")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O atributo Nome é Obrigatório")
	@Size(min = 3, max = 100, message = "Tamanho minimo: 5, maximo: 100")
	private String nome;

	@NotBlank(message = "O atributo Senha é Obrigatório")
	@Size(min = 8, message = "Tamanho minimo: 8")
	private String senha;

	@NotBlank(message="O campo tipo do usuário é Obrigatório")
	private String tipo;

	@NotBlank(message = "O atributo Email é Obrigatório")
	@Size(min = 10, max = 1000, message = "Tamanho minimo: 10, maximo: 1000")
	@Email
	private String email;

	@Size(min = 2, max = 10000, message = "Tamanho minimo: 2, maximo: 1000")
	private String foto;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("usuario")
	private List<Product> product;

	public List<Product> getProduto() {
		return product;
	}

	public void setProduto(List<Product> product) {
		this.product = product;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

}
