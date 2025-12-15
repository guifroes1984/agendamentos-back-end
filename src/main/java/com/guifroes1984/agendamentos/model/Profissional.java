package com.guifroes1984.agendamentos.model;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_PROFISSIONAIS")
public class Profissional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@Column(length = 500)
	private String especialidade;

	@Column(name = "foto_url")
	private String fotoUrl;

	@Column(name = "experiencia_anos")
	private Integer experienciaAnos;

	@ElementCollection
	@CollectionTable(name = "profissional_horarios", joinColumns = @JoinColumn(name = "profissional_id"))
	@Column(name = "dia_semana")
	@Enumerated(EnumType.STRING)
	private Set<DiaSemana> diasTrabalho = new HashSet<>();

	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	@Column(name = "hora_fim")
	private LocalTime horaFim;

	@Column(name = "intervalo_minutos")
	private Integer intervaloMinutos = 15;

	@Column(nullable = false)
	private Boolean ativo = true;

	public enum DiaSemana {
		SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO
	}

	public Profissional() {
		this.ativo = true;
		this.intervaloMinutos = 15;
		this.diasTrabalho = new HashSet<>();
	}

	public Profissional(Usuario usuario, String especialidade) {
		this.usuario = usuario;
		this.especialidade = especialidade;
		this.ativo = true;
		this.intervaloMinutos = 15;
		this.diasTrabalho = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
	}

	public Integer getExperienciaAnos() {
		return experienciaAnos;
	}

	public void setExperienciaAnos(Integer experienciaAnos) {
		this.experienciaAnos = experienciaAnos;
	}

	public Set<DiaSemana> getDiasTrabalho() {
		return diasTrabalho;
	}

	public void setDiasTrabalho(Set<DiaSemana> diasTrabalho) {
		this.diasTrabalho = diasTrabalho;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(LocalTime horaFim) {
		this.horaFim = horaFim;
	}

	public Integer getIntervaloMinutos() {
		return intervaloMinutos;
	}

	public void setIntervaloMinutos(Integer intervaloMinutos) {
		this.intervaloMinutos = intervaloMinutos;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profissional other = (Profissional) obj;
		return Objects.equals(id, other.id);
	}

}
