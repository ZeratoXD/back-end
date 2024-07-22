package com.generation.NossoPomar.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    // Configuração do serviço que fornece detalhes do usuário para autenticação
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // Configuração do encoder de senhas utilizando BCrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração do provedor de autenticação DAO
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // Configuração do gerenciador de autenticação
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuração da cadeia de filtros de segurança HTTP
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Define a política de criação de sessões como stateless (sem estado)
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable()) // Desabilita a proteção CSRF
                .cors(withDefaults()); // Habilita CORS com configurações padrão

        // Configuração das regras de autorização HTTP
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/users/login").permitAll() // Permite acesso público à rota de login
                .requestMatchers("/products").permitAll() // Permite acesso público à rota de produtos
                .requestMatchers("/users/register").permitAll() // Permite acesso público à rota de registro
                .requestMatchers("/error/**").permitAll() // Permite acesso público à rota de erro
                .requestMatchers(HttpMethod.OPTIONS).permitAll() // Permite acesso público a requisições OPTIONS
                .anyRequest().authenticated()) // Exige autenticação para todas as outras requisições
                .authenticationProvider(authenticationProvider()) // Define o provedor de autenticação
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro JWT antes do filtro de autenticação padrão
                .httpBasic(withDefaults()); // Habilita autenticação HTTP básica com configurações padrão

        return http.build(); // Constrói a cadeia de filtros de segurança
    }

}
