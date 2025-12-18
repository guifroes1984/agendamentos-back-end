package com.guifroes1984.agendamentos.dto.resquest;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class AgendamentoRequest {

	@NotNull(message = "Cliente ID é obrigatório")
	private Long clienteId;

	@NotNull(message = "Profissional ID é obrigatório")
	private Long profissionalId;

	@NotNull(message = "Serviço ID é obrigatório")
	private Long servicoId;

	@NotNull(message = "Data e hora são obrigatórias")
	@Future(message = "Data deve ser futura")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataHoraInicio;

	private String observacoes;

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Long getProfissionalId() {
		return profissionalId;
	}

	public void setProfissionalId(Long profissionalId) {
		this.profissionalId = profissionalId;
	}

	public Long getServicoId() {
		return servicoId;
	}

	public void setServicoId(Long servicoId) {
		this.servicoId = servicoId;
	}

	public LocalDateTime getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

}
