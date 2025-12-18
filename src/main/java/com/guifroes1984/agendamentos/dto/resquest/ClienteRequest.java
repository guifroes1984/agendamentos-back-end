package com.guifroes1984.agendamentos.dto.resquest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClienteRequest {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email inválido")
	private String email;

	@Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone inválido. Use (11) 99999-9999 ou 11999999999")
	private String telefone;

	private String observacoes;

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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

}
