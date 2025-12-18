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

import com.guifroes1984.agendamentos.model.Agendamento;
import com.guifroes1984.agendamentos.service.AgendamentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos")
public class AgendamentoController {

	@Autowired
	private AgendamentoService agendamentoService;

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

	@Operation(summary = "Buscar agendamentos futuros por cliente")
	@GetMapping("/cliente/{clienteId}/futuros")
	public ResponseEntity<List<Agendamento>> buscarFuturosPorCliente(@PathVariable Long clienteId) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarFuturosPorCliente(clienteId);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamentos passados por cliente")
	@GetMapping("/cliente/{clienteId}/passados")
	public ResponseEntity<List<Agendamento>> buscarPassadosPorCliente(@PathVariable Long clienteId) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarPassadosPorCliente(clienteId);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamentos por profissional")
	@GetMapping("/profissional/{profissionalId}")
	public ResponseEntity<List<Agendamento>> buscarPorProfissional(@PathVariable Long profissionalId) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarPorProfissional(profissionalId);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamentos futuros por profissional")
	@GetMapping("/profissional/{profissionalId}/futuros")
	public ResponseEntity<List<Agendamento>> buscarFuturosPorProfissional(@PathVariable Long profissionalId) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarFuturosPorProfissional(profissionalId);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar agendamentos por status")
	@GetMapping("/status/{status}")
	public ResponseEntity<List<Agendamento>> buscarPorStatus(
			@Parameter(description = "Status do agendamento", example = "AGENDADO") 
			@PathVariable String status) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarPorStatus(status);
			return ResponseEntity.ok(agendamentos);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Criar novo agendamento")
    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso",
        content = @Content(schema = @Schema(implementation = Agendamento.class)))
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

	@Operation(summary = "Atualizar agendamento")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Agendamento agendamentoAtualizado) {
		try {
			Optional<Agendamento> agendamento = agendamentoService.atualizar(id, agendamentoAtualizado);

			if (agendamento.isPresent()) {
				return ResponseEntity.ok(agendamento.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar agendamento: " + e.getMessage());
		}
	}

	@Operation(summary = "Atualizar status do agendamento")
	@PutMapping("/{id}/status")
	public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
		try {
			Optional<Agendamento> agendamento = agendamentoService.atualizarStatus(id, status);

			if (agendamento.isPresent()) {
				return ResponseEntity.ok(agendamento.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar status: " + e.getMessage());
		}
	}

	@Operation(summary = "Cancelar agendamento")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> cancelar(@PathVariable Long id) {
		try {
			boolean cancelado = agendamentoService.cancelar(id);

			if (cancelado) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao cancelar agendamento: " + e.getMessage());
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

	@Operation(summary = "Buscar agendamentos por período")
	@GetMapping("/periodo")
	public ResponseEntity<List<Agendamento>> buscarPorPeriodo(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
		try {
			List<Agendamento> agendamentos = agendamentoService.buscarPorPeriodo(inicio, fim);
			return ResponseEntity.ok(agendamentos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
