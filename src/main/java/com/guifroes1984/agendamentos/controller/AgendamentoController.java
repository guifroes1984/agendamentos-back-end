package com.guifroes1984.agendamentos.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.agendamentos.dto.response.AgendamentoResponse;
import com.guifroes1984.agendamentos.dto.resquest.AgendamentoRequest;
import com.guifroes1984.agendamentos.model.Agendamento;
import com.guifroes1984.agendamentos.service.AgendamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos")
public class AgendamentoController {

	@Autowired
	private AgendamentoService agendamentoService;

	@Operation(summary = "Listar todos os agendamentos DTO")
	@ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
	@GetMapping("/dto")
	public ResponseEntity<List<AgendamentoResponse>> listarTodosDTO() {
		try {
			List<AgendamentoResponse> agendamentos = agendamentoService.listarTodosResponse();
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamento por ID DTO")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agendamento encontrado"),
			@ApiResponse(responseCode = "404", description = "Agendamento não encontrado") })
	@GetMapping("/dto/{id}")
	public ResponseEntity<AgendamentoResponse> buscarPorIdDTO(@PathVariable Long id) {
		try {
			AgendamentoResponse agendamento = agendamentoService.buscarPorIdResponse(id);
			return ResponseEntity.ok(agendamento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Criar novo agendamento DTO")
	@ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso", content = @Content(schema = @Schema(implementation = AgendamentoResponse.class)))
	@PostMapping("/dto")
	public ResponseEntity<?> criarDTO(@Valid @RequestBody AgendamentoRequest request) {
		try {
			AgendamentoResponse agendamento = agendamentoService.criar(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(agendamento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar agendamento: " + e.getMessage());
		}
	}

	@Operation(summary = "Buscar agendamentos por cliente DTO")
	@GetMapping("/dto/cliente/{clienteId}")
	public ResponseEntity<List<AgendamentoResponse>> buscarPorClienteDTO(
			@Parameter(description = "ID do cliente", example = "5") @PathVariable Long clienteId) {
		try {
			List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorClienteResponse(clienteId);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Atualizar status do agendamento DTO")
	@PutMapping("/dto/{id}/status")
	public ResponseEntity<?> atualizarStatusDTO(@PathVariable Long id, @RequestParam String status) {
		try {
			AgendamentoResponse agendamento = agendamentoService.atualizarStatusResponse(id, status);
			return ResponseEntity.ok(agendamento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar status: " + e.getMessage());
		}
	}

	@Operation(summary = "Cancelar agendamento DTO")
	@DeleteMapping("/dto/{id}")
	public ResponseEntity<?> cancelarDTO(@PathVariable Long id) {
		try {
			AgendamentoResponse agendamento = agendamentoService.cancelarResponse(id);
			return ResponseEntity.ok(agendamento);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao cancelar agendamento: " + e.getMessage());
		}
	}

	@Operation(summary = "Buscar agendamentos por período DTO")
	@GetMapping("/dto/periodo")
	public ResponseEntity<List<AgendamentoResponse>> buscarPorPeriodoDTO(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
		try {
			List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorPeriodoResponse(inicio, fim);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Listar todos os agendamentos")
	@ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<Agendamento>> listarTodos() {
		try {
			List<Agendamento> agendamentos = agendamentoService.listarTodos();
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamento por ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Agendamento encontrado"),
			@ApiResponse(responseCode = "404", description = "Agendamento não encontrado") })
	@GetMapping("/{id}")
	public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
		try {
			Optional<Agendamento> agendamento = agendamentoService.buscarPorId(id);

			if (agendamento.isPresent()) {
				return ResponseEntity.ok(agendamento.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamentos por cliente")
	@GetMapping("/cliente/{clienteId}")
	public ResponseEntity<List<Agendamento>> buscarPorCliente(
			@Parameter(description = "ID do cliente", example = "5") @PathVariable Long clienteId) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarPorCliente(clienteId);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Criar novo agendamento")
	@ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso", content = @Content(schema = @Schema(implementation = Agendamento.class)))
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Agendamento agendamento) {
		try {
			Agendamento agendamentoSalvo = agendamentoService.criar(agendamento);
			return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar agendamento: " + e.getMessage());
		}
	}

	@Operation(summary = "Verificar disponibilidade do profissional")
	@GetMapping("/disponibilidade")
	public ResponseEntity<?> verificarDisponibilidade(@RequestParam Long profissionalId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
		try {
			boolean disponivel = agendamentoService.verificarDisponibilidade(profissionalId, inicio, fim);
			return ResponseEntity.ok(disponivel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao verificar disponibilidade: " + e.getMessage());
		}
	}
}
