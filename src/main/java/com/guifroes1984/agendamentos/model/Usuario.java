package com.guifroes1984.agendamentos.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_USUARIOS")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String senha;

	@Column(nullable = false)
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private Boolean ativo = true;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	public enum Role {
		ADMIN, PROFISSIONAL, CLIENTE
	}

	// Construtores
	public Usuario() {
		this.ativo = true;
		this.dataCriacao = LocalDateTime.now();
	}

	public Usuario(String nome, String email, String senha, Role role) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
		this.ativo = true;
		this.dataCriacao = LocalDateTime.now();
	}

	// Métodos do UserDetails
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return ativo;
	}

	// PrePersist para garantir dataCriacao
	@PrePersist
	protected void onCreate() {
		if (dataCriacao == null) {
			dataCriacao = LocalDateTime.now();
		}
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	// Método estático para builder pattern (opcional)
	public static Builder builder() {
		return new Builder();
	}

	// Builder Pattern (opcional, mas útil)
	public static class Builder {
		private Usuario usuario;

		public Builder() {
			usuario = new Usuario();
		}

		public Builder nome(String nome) {
			usuario.setNome(nome);
			return this;
		}

		public Builder email(String email) {
			usuario.setEmail(email);
			return this;
		}

		public Builder senha(String senha) {
			usuario.setSenha(senha);
			return this;
		}

		public Builder role(Role role) {
			usuario.setRole(role);
			return this;
		}

		public Builder ativo(Boolean ativo) {
			usuario.setAtivo(ativo);
			return this;
		}

		public Usuario build() {
			return usuario;
		}
	}
}
