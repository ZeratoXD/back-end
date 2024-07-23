package com.generation.NossoPomar.security;

public class PomarToken {
	
	private String token;

	public PomarToken(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "PomarToken [token=" + token + "]";
	}
}
