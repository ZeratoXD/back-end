package com.generation.NossoPomar.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	// Obtém o header "Authorization" da requisição
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		try {
			// Verifica se o header possui um token JWT com o prefixo "Bearer "
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				// Extrai o token JWT do header
				token = authHeader.substring(7);
				// Extrai o nome de usuário do token JWT
				username = jwtService.extractUsername(token);
			}
			// Verifica se o nome de usuário foi extraído e se não há uma autenticação
			// existente no contexto de segurança
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// Carrega os detalhes do usuário a partir do nome de usuário
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				// Valida o token JWT
				if (jwtService.validateToken(token, userDetails)) {
					// Cria um token de autenticação e define os detalhes da requisição
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// Define a autenticação no contexto de segurança
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}

			}
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| ResponseStatusException e) {
			// Define o status de resposta como FORBIDDEN se houver uma exceção relacionada ao JWT
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
	}
}
