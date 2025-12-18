package com.guifroes1984.agendamentos.dto.response;

import java.math.BigDecimal;

import com.guifroes1984.agendamentos.model.Servico;

public class ServicoResponse {

	private Long id;
	private String nome;
	private String descricao;
	private Integer duracaoMinutos;
	private BigDecimal preco;
	private String categoria;
	private Boolean ativo;

	public ServicoResponse(Servico servico) {
		this.id = servico.getId();
		this.nome = servico.getNome();
		this.descricao = servico.getDescricao();
		this.duracaoMinutos = servico.getDuracaoMinutos();
		this.preco = servico.getPreco();
		this.categoria = servico.getCategoria() != null ? servico.getCategoria().name() : null;
		this.ativo = servico.getAtivo();
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
