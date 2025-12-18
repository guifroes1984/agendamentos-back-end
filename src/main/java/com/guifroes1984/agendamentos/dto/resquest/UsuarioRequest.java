package com.guifroes1984.agendamentos.dto.resquest;

import com.guifroes1984.agendamentos.model.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRequest {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email inválido")
	private String email;

	@NotBlank(message = "Senha é obrigatória")
	private String senha;

	private Usuario.Role role = Usuario.Role.CLIENTE;

	private Boolean ativo = true;

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

	public Usuario.Role getRole() {
		return role;
	}

	public void setRole(Usuario.Role role) {
		this.role = role;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
