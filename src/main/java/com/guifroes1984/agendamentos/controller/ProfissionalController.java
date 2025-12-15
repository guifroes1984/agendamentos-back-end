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

import com.guifroes1984.agendamentos.model.Profissional;
import com.guifroes1984.agendamentos.service.ProfissionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
@Tag(name = "Profissionais", description = "Gerenciamento de profissionais (manicures, pedicures, etc.)")
public class ProfissionalController {

	@Autowired
	private ProfissionalService profissionalService;

	@Operation(summary = "Listar todos os profissionais")
	@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<Profissional>> listarTodos() {
		try {
			List<Profissional> profissionais = profissionalService.listarTodos();
			return ResponseEntity.ok(profissionais);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Listar apenas profissionais ativos")
	@GetMapping("/ativos")
	public ResponseEntity<List<Profissional>> listarAtivos() {
		try {
			List<Profissional> profissionais = profissionalService.listarAtivos();
			return ResponseEntity.ok(profissionais);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar profissional por ID")
	@ApiResponse(responseCode = "200", description = "Profissional encontrado")
	@ApiResponse(responseCode = "404", description = "Profissional não encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<Profissional> buscarPorId(@PathVariable Long id) {
		try {
			Optional<Profissional> profissional = profissionalService.buscarPorId(id);

			if (profissional.isPresent()) {
				return ResponseEntity.ok(profissional.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar profissional por e-mail")
	@GetMapping("/email/{email}")
	public ResponseEntity<Profissional> buscarPorEmail(@PathVariable String email) {
		try {
			Optional<Profissional> profissional = profissionalService.buscarPorEmail(email);

			if (profissional.isPresent()) {
				return ResponseEntity.ok(profissional.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Cadastrar novo profissional", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = Profissional.class))))
	@ApiResponse(responseCode = "201", description = "Profissional criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Profissional profissional) {
		try {
			Profissional profissionalSalvo = profissionalService.criar(profissional);
			return ResponseEntity.status(HttpStatus.CREATED).body(profissionalSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar profissional: " + e.getMessage());
		}
	}

	@Operation(summary = "Atualizar profissional existente")
    @ApiResponse(responseCode = "200", description = "Profissional atualizado")
    @ApiResponse(responseCode = "404", description = "Profissional não encontrado")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Profissional profissionalAtualizado) {
		try {
			Optional<Profissional> profissional = profissionalService.atualizar(id, profissionalAtualizado);

			if (profissional.isPresent()) {
				return ResponseEntity.ok(profissional.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar profissional: " + e.getMessage());
		}
	}

	@Operation(summary = "Excluir profissional (soft delete)")
    @ApiResponse(responseCode = "204", description = "Profissional removido")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			boolean deletado = profissionalService.deletar(id);

			if (deletado) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar profissionais por especialidade")
	@GetMapping("/especialidade/{especialidade}")
	public ResponseEntity<List<Profissional>> buscarPorEspecialidade(@PathVariable String especialidade) {
		try {
			List<Profissional> profissionais = profissionalService.buscarPorEspecialidade(especialidade);
			return ResponseEntity.ok(profissionais);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
