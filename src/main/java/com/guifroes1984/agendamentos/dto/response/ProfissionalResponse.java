package com.guifroes1984.agendamentos.dto.response;

import java.time.LocalTime;
import java.util.Set;

import com.guifroes1984.agendamentos.model.Profissional;

public class ProfissionalResponse {

	private Long id;
	private String nome;
	private String email;
	private String especialidade;
	private String fotoUrl;
	private Integer experienciaAnos;
	private Set<Profissional.DiaSemana> diasTrabalho;
	private LocalTime horaInicio;
	private LocalTime horaFim;
	private Integer intervaloMinutos;
	private Boolean ativo;
	
	public ProfissionalResponse(Profissional profissional) {
		this.id = profissional.getId();
		this.nome = profissional.getUsuario().getNome();
		this.email = profissional.getUsuario().getEmail();
		this.especialidade = profissional.getEspecialidade();
		this.fotoUrl = profissional.getFotoUrl();
		this.experienciaAnos = profissional.getExperienciaAnos();
		this.diasTrabalho = profissional.getDiasTrabalho();
		this.horaInicio = profissional.getHoraInicio();
		this.horaFim = profissional.getHoraFim();
		this.intervaloMinutos = profissional.getIntervaloMinutos();
		this.ativo = profissional.getAtivo();
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

	public Set<Profissional.DiaSemana> getDiasTrabalho() {
		return diasTrabalho;
	}

	public void setDiasTrabalho(Set<Profissional.DiaSemana> diasTrabalho) {
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

}
