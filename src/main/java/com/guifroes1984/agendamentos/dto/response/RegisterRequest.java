package com.guifroes1984.agendamentos.dto.response;

import com.guifroes1984.agendamentos.model.Usuario.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email deve ser válido")
	private String email;

	@NotBlank(message = "Senha é obrigatória")
	private String senha;

	@NotNull(message = "Role é obrigatória")
	private Role role;

	// Construtores
	public RegisterRequest() {
	}

	public RegisterRequest(String nome, String email, String senha, Role role) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
