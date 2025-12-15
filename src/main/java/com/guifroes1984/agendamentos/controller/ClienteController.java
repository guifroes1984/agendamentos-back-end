package com.guifroes1984.agendamentos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.agendamentos.model.Cliente;
import com.guifroes1984.agendamentos.repository.ClienteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Gerenciamento de clientes do sistema")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Operation(summary = "Listar todos os clientes")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
	@ApiResponse(responseCode = "500", description = "Erro interno no servidor") })
	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodos() {
		try {
			List<Cliente> clientes = clienteRepository.findAll();
			return ResponseEntity.ok(clientes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar cliente por ID")
	@ApiResponses({
	@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		try {
			Optional<Cliente> cliente = clienteRepository.findById(id);

			if (cliente.isPresent()) {
				return ResponseEntity.ok(cliente.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Criar um novo cliente")
	@ApiResponses({
	@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
	@ApiResponse(responseCode = "400", description = "Dados inválidos"),
	@ApiResponse(responseCode = "409", description = "Email ou telefone já cadastrado"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Cliente cliente) {
		try {
			if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Email é obrigatório");
			}

			if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Nome é obrigatório");
			}

			if (clienteRepository.existsByEmail(cliente.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado: " + cliente.getEmail());
			}

			if (cliente.getTelefone() != null && !cliente.getTelefone().trim().isEmpty()
					&& clienteRepository.existsByTelefone(cliente.getTelefone())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("Telefone já cadastrado: " + cliente.getTelefone());
			}

			Cliente clienteSalvo = clienteRepository.save(cliente);
			return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar cliente: " + e.getMessage());
		}
	}

	@Operation(summary = "Atualizar cliente existente")
	@ApiResponses({
	@ApiResponse(responseCode = "200", description = "Cliente atualizado"),
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
	@ApiResponse(responseCode = "409", description = "Email já em uso"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
		try {
			Optional<Cliente> clienteExistente = clienteRepository.findById(id);

			if (clienteExistente.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Cliente cliente = clienteExistente.get();

			if (!cliente.getEmail().equals(clienteAtualizado.getEmail())
					&& clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("Email já cadastrado: " + clienteAtualizado.getEmail());
			}

			cliente.setNome(clienteAtualizado.getNome());
			cliente.setEmail(clienteAtualizado.getEmail());
			cliente.setTelefone(clienteAtualizado.getTelefone());
			cliente.setObservacoes(clienteAtualizado.getObservacoes());

			Cliente clienteAtualizadoSalvo = clienteRepository.save(cliente);
			return ResponseEntity.ok(clienteAtualizadoSalvo);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar cliente: " + e.getMessage());
		}
	}

	@Operation(summary = "Excluir cliente por ID")
	@ApiResponses({
	@ApiResponse(responseCode = "204", description = "Cliente removido"),
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			if (!clienteRepository.existsById(id)) {
				return ResponseEntity.notFound().build();
			}

			clienteRepository.deleteById(id);
			return ResponseEntity.noContent().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
		try {
			Optional<Cliente> cliente = clienteRepository.findByEmail(email);

			if (cliente.isPresent()) {
				return ResponseEntity.ok(cliente.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
