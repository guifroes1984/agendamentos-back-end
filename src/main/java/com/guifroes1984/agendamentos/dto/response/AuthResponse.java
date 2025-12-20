package com.guifroes1984.agendamentos.dto.response;

import com.guifroes1984.agendamentos.model.Usuario.Role;

public class AuthResponse {
	private String token;
	private String tipo = "Bearer";
	private Long id;
	private String nome;
	private String email;
	private Role role;
	private Boolean ativo;

	// Construtores
	public AuthResponse() {
	}

	public AuthResponse(String token, Long id, String nome, String email, Role role, Boolean ativo) {
		this.token = token;
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.role = role;
		this.ativo = ativo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
