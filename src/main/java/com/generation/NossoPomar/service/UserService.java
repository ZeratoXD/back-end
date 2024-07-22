package com.generation.NossoPomar.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.NossoPomar.model.UserLogin;
import com.generation.NossoPomar.model.User;
import com.generation.NossoPomar.repository.UserRepository;
import com.generation.NossoPomar.security.JwtService;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Método para cadastrar um novo usuário
    public Optional<User> cadastrarUsuario(User user) {

        // Verifica se o email já está registrado
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            return Optional.empty();

        // Criptografa a senha do usuário
        user.setPassword(criptografarSenha(user.getPassword()));

        // Salva o usuário no repositório
        return Optional.of(userRepository.save(user));
    }

    // Método para atualizar os dados de um usuário existente
    public Optional<User> atualizarUsuario(User user) {

        // Verifica se o usuário existe pelo ID
        if (userRepository.findById(user.getId()).isPresent()) {

            // Verifica se o email já está em uso por outro usuário
            Optional<User> buscaUsuario = userRepository.findByEmail(user.getEmail());

            if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != user.getId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!", null);

            // Criptografa a nova senha do usuário
            user.setPassword(criptografarSenha(user.getPassword()));

            // Salva as atualizações do usuário no repositório
            return Optional.ofNullable(userRepository.save(user));
        }

        return Optional.empty();
    }

    // Método para autenticar um usuário
    public Optional<UserLogin> autenticarUsuario(Optional<UserLogin> userLogin) {

        // Cria um token de autenticação com o email e senha fornecidos
        var credenciais = new UsernamePasswordAuthenticationToken(userLogin.get().getEmail(),
                userLogin.get().getPassword());

        // Autentica as credenciais do usuário
        Authentication authentication = authenticationManager.authenticate(credenciais);

        // Se a autenticação for bem-sucedida
        if (authentication.isAuthenticated()) {

            // Busca o usuário pelo email
            Optional<User> user = userRepository.findByEmail(userLogin.get().getEmail());

            if (user.isPresent()) {

                // Define os dados do usuário no objeto UserLogin
                userLogin.get().setId(user.get().getId());
                userLogin.get().setName(user.get().getName());
                userLogin.get().setEmail(user.get().getEmail());
                userLogin.get().setPhoto(user.get().getPhoto());
                userLogin.get().setType(user.get().getType());
                userLogin.get().setToken(gerarToken(userLogin.get().getEmail()));
                userLogin.get().setPassword("");

                return userLogin;
            }
        }

        return Optional.empty();
    }

    // Método privado para criptografar a senha do usuário
    private String criptografarSenha(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    // Método privado para gerar um token JWT para o usuário
    private String gerarToken(String user) {
        return "Bearer " + jwtService.generateToken(user);
    }
}
