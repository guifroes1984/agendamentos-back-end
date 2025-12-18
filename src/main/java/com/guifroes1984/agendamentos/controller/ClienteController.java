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

import com.guifroes1984.agendamentos.dto.response.ClienteResponse;
import com.guifroes1984.agendamentos.dto.resquest.ClienteRequest;
import com.guifroes1984.agendamentos.model.Cliente;
import com.guifroes1984.agendamentos.repository.ClienteRepository;
import com.guifroes1984.agendamentos.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Gerenciamento de clientes do sistema")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;

	@Operation(summary = "Listar todos os clientes DTO")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso DTO"),
	@ApiResponse(responseCode = "500", description = "Erro interno no servidor") })
	@GetMapping
	public ResponseEntity<List<ClienteResponse>> listarTodos() {
		List<ClienteResponse> clientes = clienteService.listarTodos();
		return ResponseEntity.ok(clientes);
	}

	@Operation(summary = "Buscar cliente por ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
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

	@Operation(summary = "Criar um novo cliente DTO")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso DTO"),
	@ApiResponse(responseCode = "400", description = "Dados inválidos"),
	@ApiResponse(responseCode = "409", description = "Email ou telefone já cadastrado"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@PostMapping
	public ResponseEntity<?> criar(@Valid @RequestBody ClienteRequest request) {
		try {
			ClienteResponse cliente = clienteService.criar(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Atualizar cliente existente DTO")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Cliente atualizado DTO"),
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
	@ApiResponse(responseCode = "409", description = "Email já em uso"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
		try {
			ClienteResponse cliente = clienteService.atualizar(id, request);
			return ResponseEntity.ok(cliente);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Excluir cliente por ID")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Cliente removido"),
	@ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
	@ApiResponse(responseCode = "500", description = "Erro interno") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			clienteService.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
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
