package com.guifroes1984.agendamentos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guifroes1984.agendamentos.model.Agendamento;

public class AgendamentoResponse {

	private Long id;
	private String clienteNome;
	private String clienteEmail;
	private String profissionalNome;
	private String servicoNome;
	private Integer duracaoMinutos;
	private BigDecimal preco;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataHoraInicio;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataHoraFim;

	private String status;
	private String observacoes;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataCriacao;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataAtualizacao;

	// Construtor a partir da entidade
	public AgendamentoResponse(Agendamento agendamento) {
		this.id = agendamento.getId();
		this.clienteNome = agendamento.getCliente().getNome();
		this.clienteEmail = agendamento.getCliente().getEmail();
		this.profissionalNome = agendamento.getProfissional().getUsuario().getNome();
		this.servicoNome = agendamento.getServico().getNome();
		this.duracaoMinutos = agendamento.getServico().getDuracaoMinutos();
		this.preco = agendamento.getServico().getPreco();
		this.dataHoraInicio = agendamento.getDataHoraInicio();
		this.dataHoraFim = agendamento.getDataHoraFim();
		this.status = agendamento.getStatus().name();
		this.observacoes = agendamento.getObservacoes();
		this.dataCriacao = agendamento.getDataCriacao();
		this.dataAtualizacao = agendamento.getDataAtualizacao();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClienteNome() {
		return clienteNome;
	}

	public void setClienteNome(String clienteNome) {
		this.clienteNome = clienteNome;
	}

	public String getClienteEmail() {
		return clienteEmail;
	}

	public void setClienteEmail(String clienteEmail) {
		this.clienteEmail = clienteEmail;
	}

	public String getProfissionalNome() {
		return profissionalNome;
	}

	public void setProfissionalNome(String profissionalNome) {
		this.profissionalNome = profissionalNome;
	}

	public String getServicoNome() {
		return servicoNome;
	}

	public void setServicoNome(String servicoNome) {
		this.servicoNome = servicoNome;
	}

	public Integer getDuracaoMinutos() {
		return duracaoMinutos;
	}

	public void setDuracaoMinutos(Integer duracaoMinutos) {
		this.duracaoMinutos = duracaoMinutos;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

}
