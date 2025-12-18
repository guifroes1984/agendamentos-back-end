package com.guifroes1984.agendamentos.controller;

import java.util.List;

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

import com.guifroes1984.agendamentos.dto.response.ProfissionalResponse;
import com.guifroes1984.agendamentos.dto.resquest.ProfissionalRequest;
import com.guifroes1984.agendamentos.model.Profissional;
import com.guifroes1984.agendamentos.service.ProfissionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
@Tag(name = "Profissionais", description = "Gerenciamento de profissionais (manicures, pedicures, etc.)")
public class ProfissionalController {

	@Autowired
	private ProfissionalService profissionalService;

	@Operation(summary = "Listar todos os profissionais DTO")
	@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<ProfissionalResponse>> listarTodos() {
		List<ProfissionalResponse> profissionais = profissionalService.listarTodos();
		return ResponseEntity.ok(profissionais);
	}

	@Operation(summary = "Listar apenas profissionais ativos DTO")
	@GetMapping("/ativos")
	public ResponseEntity<List<ProfissionalResponse>> listarAtivos() {
		List<ProfissionalResponse> profissionais = profissionalService.listarAtivos();
		return ResponseEntity.ok(profissionais);
	}

	@Operation(summary = "Buscar profissional por ID com DTO")
	@ApiResponse(responseCode = "200", description = "Profissional encontrado")
	@ApiResponse(responseCode = "404", description = "Profissional não encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<ProfissionalResponse> buscarPorId(@PathVariable Long id) {
		try {
			ProfissionalResponse profissional = profissionalService.buscarPorId(id);
			return ResponseEntity.ok(profissional);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Buscar profissional por e-mail DTO")
	@GetMapping("/email/{email}")
	public ResponseEntity<ProfissionalResponse> buscarPorEmail(@PathVariable String email) {
		try {
			ProfissionalResponse profissional = profissionalService.buscarPorEmail(email);
			return ResponseEntity.ok(profissional);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Cadastrar novo profissional DTO", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = Profissional.class))))
	@ApiResponse(responseCode = "201", description = "Profissional criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	@PostMapping
	public ResponseEntity<?> criar(@Valid @RequestBody ProfissionalRequest request) {
		try {
			ProfissionalResponse profissional = profissionalService.criar(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Atualizar profissional existente DTO")
	@ApiResponse(responseCode = "200", description = "Profissional atualizado")
	@ApiResponse(responseCode = "404", description = "Profissional não encontrado")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ProfissionalRequest request) {
		try {
			ProfissionalResponse profissional = profissionalService.atualizar(id, request);
			return ResponseEntity.ok(profissional);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Excluir profissional (soft delete)")
	@ApiResponse(responseCode = "204", description = "Profissional removido")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			profissionalService.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Buscar profissionais por especialidade DTO")
	@GetMapping("/especialidade/{especialidade}")
	public ResponseEntity<List<ProfissionalResponse>> buscarPorEspecialidade(@PathVariable String especialidade) {
		List<ProfissionalResponse> profissionais = profissionalService.buscarPorEspecialidade(especialidade);
		return ResponseEntity.ok(profissionais);
	}
}
