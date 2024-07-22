package com.generation.NossoPomar.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.NossoPomar.model.User;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    // Construtor que inicializa os campos userName e password a partir de um objeto User
    public UserDetailsImpl(User user) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
    }

    // Construtor padrão
    public UserDetailsImpl() { }

    // Retorna as autoridades concedidas ao usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Retorna a senha do usuário
    @Override
    public String getPassword() {
        return password;
    }

    // Retorna o nome de usuário (email) do usuário
    @Override
    public String getUsername() {
        return userName;
    }

    // Indica se a conta do usuário não está expirada
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indica se a conta do usuário não está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indica se as credenciais do usuário (senha) não estão expiradas
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indica se a conta do usuário está habilitada
    @Override
    public boolean isEnabled() {
        return true;
    }

}
