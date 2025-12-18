package com.guifroes1984.agendamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guifroes1984.agendamentos.dto.response.ClienteResponse;
import com.guifroes1984.agendamentos.dto.resquest.ClienteRequest;
import com.guifroes1984.agendamentos.model.Cliente;
import com.guifroes1984.agendamentos.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public List<ClienteResponse> listarTodos() {
		return clienteRepository.findAll().stream().map(ClienteResponse::new).collect(Collectors.toList());
	}

	public ClienteResponse buscarPorId(Long id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
		return new ClienteResponse(cliente);
	}

	@Transactional
	public ClienteResponse criar(ClienteRequest request) {

		if (clienteRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email já cadastrado: " + request.getEmail());
		}

		Cliente cliente = new Cliente();
		cliente.setNome(request.getNome());
		cliente.setEmail(request.getEmail());
		cliente.setTelefone(request.getTelefone());
		cliente.setObservacoes(request.getObservacoes());

		Cliente salvo = clienteRepository.save(cliente);
		return new ClienteResponse(salvo);
	}

	@Transactional
	public ClienteResponse atualizar(Long id, ClienteRequest request) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

		if (!cliente.getEmail().equals(request.getEmail()) && clienteRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email já cadastrado: " + request.getEmail());
		}

		cliente.setNome(request.getNome());
		cliente.setEmail(request.getEmail());
		cliente.setTelefone(request.getTelefone());
		cliente.setObservacoes(request.getObservacoes());

		Cliente atualizado = clienteRepository.save(cliente);
		return new ClienteResponse(atualizado);
	}

	@Transactional
	public void deletar(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw new IllegalArgumentException("Cliente não encontrado");
		}
		clienteRepository.deleteById(id);
	}

	public ClienteResponse toResponse(Cliente cliente) {
		return new ClienteResponse(cliente);
	}
}
