package com.guifroes1984.agendamentos.dto.resquest;

import java.math.BigDecimal;

import com.guifroes1984.agendamentos.model.Servico;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ServicoRequest {

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	private String descricao;

	@NotNull(message = "Duração é obrigatória")
	@Min(value = 1, message = "Duração deve ser pelo menos 1 minuto")
	private Integer duracaoMinutos;

	@NotNull(message = "Preço é obrigatório")
	@Positive(message = "Preço deve ser positivo")
	private BigDecimal preco;

	private Servico.CategoriaServico categoria;

	private Boolean ativo = true;

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

	public Servico.CategoriaServico getCategoria() {
		return categoria;
	}

	public void setCategoria(Servico.CategoriaServico categoria) {
		this.categoria = categoria;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
