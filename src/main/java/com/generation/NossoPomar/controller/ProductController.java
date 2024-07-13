package com.generation.NossoPomar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.server.ResponseStatusException;

import com.generation.NossoPomar.model.Product;
import com.generation.NossoPomar.repository.CategoryRepository;
import com.generation.NossoPomar.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public ResponseEntity<List<Product>> getAll() {
		return ResponseEntity.ok(productRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getById(@PathVariable Long id) {
		return productRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/produto/{nome}")
	public ResponseEntity<List<Product>> getById(@PathVariable String nome) {
		return ResponseEntity.ok(productRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@PostMapping
	public ResponseEntity<Product> post(@Valid @RequestBody Product product) {
		if (categoryRepository.existsById(product.getCategoria().getId())) {
			return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
		}

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category não existe!", null);
	}

	@PutMapping
	public ResponseEntity<Product> put(@Valid @RequestBody Product product) {
		if (productRepository.existsById(product.getId())) {

			if (categoryRepository.existsById(product.getCategoria().getId())) {
				return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
			}

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Não Existe!", null);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Product> postagem = productRepository.findById(id);

		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		productRepository.deleteById(id);
	}

}
