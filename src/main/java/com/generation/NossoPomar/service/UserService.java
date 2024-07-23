package com.generation.NossoPomar.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.NossoPomar.dto.UserLogin;
import com.generation.NossoPomar.model.User;
import com.generation.NossoPomar.repository.UserRepository;
import com.generation.NossoPomar.security.PomarToken;
import com.generation.NossoPomar.security.TokenUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository usuarioRepository;


	/*@Autowired
	private AuthenticationManager authenticationManager;*/

	public Optional<User> cadastrarUsuario(User usuario) {

		if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
			return Optional.empty();

		usuario.setPassword(criptografarSenha(usuario.getPassword()));

		return Optional.of(usuarioRepository.save(usuario));

	}

	public Optional<User> atualizarUsuario(User usuario) {

		if (usuarioRepository.findById(usuario.getId()).isPresent()) {

			Optional<User> buscaUsuario = usuarioRepository.findByEmail(usuario.getEmail());

			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setPassword(criptografarSenha(usuario.getPassword()));

			return Optional.ofNullable(usuarioRepository.save(usuario));

		}

		return Optional.empty();

	}

	public PomarToken autenticarUsuario(UserLogin data) {
		
		Optional<User> res = usuarioRepository.findByEmail(data.getEmail());
		
		if(res.isPresent()) {
			
			User existingUser = res.get();
			
			BCryptPasswordEncoder verifyPass = new BCryptPasswordEncoder();
						
			if(verifyPass.matches(data.getPassword(), existingUser.getPassword())) {
				PomarToken token = TokenUtil.encode(existingUser);
				return token;
			}
			System.err.println("Senha Incorreta. Verfique as informações e tente novamente.");
		}
		
		System.err.println("Usuário não existe no banco de dados");
		return null;		
	}

	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);

	}
	
}
	
	