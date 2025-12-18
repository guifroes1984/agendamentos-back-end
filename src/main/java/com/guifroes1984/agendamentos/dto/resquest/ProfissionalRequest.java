package com.guifroes1984.agendamentos.dto.resquest;

import java.time.LocalTime;
import java.util.Set;

import com.guifroes1984.agendamentos.model.Profissional;

import jakarta.validation.constraints.NotNull;

public class ProfissionalRequest {

	@NotNull(message = "Usuário ID é obrigatório")
	private Long usuarioId;

	private String especialidade;

	private String fotoUrl;

	private Integer experienciaAnos;

	private Set<Profissional.DiaSemana> diasTrabalho;

	@NotNull(message = "Hora início é obrigatória")
	private LocalTime horaInicio;

	@NotNull(message = "Hora fim é obrigatória")
	private LocalTime horaFim;

	private Integer intervaloMinutos = 15;

	private Boolean ativo = true;

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
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
