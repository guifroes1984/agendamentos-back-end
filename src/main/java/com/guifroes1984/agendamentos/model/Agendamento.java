package com.guifroes1984.agendamentos.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_AGENDAMENTOS")
public class Agendamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "profissional_id", nullable = false)
	private Profissional profissional;

	@ManyToOne
	@JoinColumn(name = "servico_id", nullable = false)
	private Servico servico;

	@Column(name = "data_hora_inicio", nullable = false)
	private LocalDateTime dataHoraInicio;

	@Column(name = "data_hora_fim")
	private LocalDateTime dataHoraFim;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusAgendamento status = StatusAgendamento.AGENDADO;

	private String observacoes;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao = LocalDateTime.now();

	public enum StatusAgendamento {
		AGENDADO, CONFIRMADO, REALIZADO, CANCELADO, FALTOU
	}

	public Agendamento() {
		this.status = StatusAgendamento.AGENDADO;
		this.dataCriacao = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
	}

	public Agendamento(Cliente cliente, Profissional profissional, Servico servico, LocalDateTime dataHoraInicio) {
		this.cliente = cliente;
		this.profissional = profissional;
		this.servico = servico;
		this.dataHoraInicio = dataHoraInicio;
		this.status = StatusAgendamento.AGENDADO;
		this.dataCriacao = LocalDateTime.now();
		this.dataAtualizacao = LocalDateTime.now();
		calcularDataHoraFim();
	}

	public void calcularDataHoraFim() {
		if (this.dataHoraInicio != null && this.servico != null && this.servico.getDuracaoMinutos() != null) {
			this.dataHoraFim = this.dataHoraInicio.plusMinutes(this.servico.getDuracaoMinutos());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public LocalDateTime getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public LocalDateTime getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(LocalDateTime dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}

	public StatusAgendamento getStatus() {
		return status;
	}

	public void setStatus(StatusAgendamento status) {
		this.status = status;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
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
		Agendamento other = (Agendamento) obj;
		return Objects.equals(id, other.id);
	}

}
