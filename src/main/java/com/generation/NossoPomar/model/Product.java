package com.generation.NossoPomar.model;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produto")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O atributo Nome é Obrigatório")
	@Size(min = 5, max = 100, message = "Tamanho minimo: 5, maximo: 100")
	private String nome;

	@NotNull(message = "O atributo Data é Obrigatório")
	private LocalDate validade;

	@NotBlank(message = "O atributo Foto é Obrigatório")
	@Size(min = 2, max = 10000, message = "Tamanho minimo: 10, maximo: 10000")
	private String foto;

	@NotBlank(message = "O atributo Descrição é Obrigatório")
	@Size(min = 5, max = 1000, message = "Tamanho minimo: 5, maximo: 1000")
	private String descricao;

	@NotNull(message = "O atributo Quantidade é Obrigatório")
	private int quantidade;

	@NotNull(message = "O atributo Preço é Obrigatório")
	private Double preco;
	
	@ManyToOne
	@JsonIgnoreProperties("produto")
	private User user;
	

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

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public Category getCategoria() {
		return category;
	}

	public void setCategoria(Category category) {
		this.category = category;
	}

	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Category category;
}
