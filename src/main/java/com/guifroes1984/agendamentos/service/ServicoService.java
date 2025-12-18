package com.guifroes1984.agendamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guifroes1984.agendamentos.dto.response.ServicoResponse;
import com.guifroes1984.agendamentos.dto.resquest.ServicoRequest;
import com.guifroes1984.agendamentos.model.Servico;
import com.guifroes1984.agendamentos.repository.ServicoRepository;

@Service
public class ServicoService {

	@Autowired
	private ServicoRepository servicoRepository;

	public List<ServicoResponse> listarTodos() {
		return servicoRepository.findAll().stream().map(ServicoResponse::new).collect(Collectors.toList());
	}

	public List<ServicoResponse> listarAtivos() {
		return servicoRepository.findByAtivoTrue().stream().map(ServicoResponse::new).collect(Collectors.toList());
	}

	public ServicoResponse buscarPorId(Long id) {
		Servico servico = servicoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));
		return new ServicoResponse(servico);
	}

	@Transactional
	public ServicoResponse criar(ServicoRequest request) {
		Servico servico = new Servico();
		servico.setNome(request.getNome());
		servico.setDescricao(request.getDescricao());
		servico.setDuracaoMinutos(request.getDuracaoMinutos());
		servico.setPreco(request.getPreco());
		servico.setCategoria(request.getCategoria());
		servico.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

		Servico salvo = servicoRepository.save(servico);
		return new ServicoResponse(salvo);
	}

	@Transactional
	public ServicoResponse atualizar(Long id, ServicoRequest request) {
		Servico servico = servicoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

		servico.setNome(request.getNome());
		servico.setDescricao(request.getDescricao());
		servico.setDuracaoMinutos(request.getDuracaoMinutos());
		servico.setPreco(request.getPreco());
		servico.setCategoria(request.getCategoria());
		servico.setAtivo(request.getAtivo());

		Servico atualizado = servicoRepository.save(servico);
		return new ServicoResponse(atualizado);
	}

	@Transactional
	public void deletar(Long id) {
		Servico servico = servicoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

		servico.setAtivo(false);
		servicoRepository.save(servico);
	}

	public List<ServicoResponse> buscarPorCategoria(String categoriaStr) {
		try {
			Servico.CategoriaServico categoria = Servico.CategoriaServico.valueOf(categoriaStr.toUpperCase());
			return servicoRepository.findByCategoria(categoria).stream().map(ServicoResponse::new)
					.collect(Collectors.toList());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Categoria inválida: " + categoriaStr);
		}
	}

	public List<ServicoResponse> buscarPorNome(String nome) {
		return servicoRepository.findByNomeContainingIgnoreCase(nome).stream().map(ServicoResponse::new)
				.collect(Collectors.toList());
	}

}
