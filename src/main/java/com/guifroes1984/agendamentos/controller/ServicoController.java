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

import com.guifroes1984.agendamentos.dto.response.ServicoResponse;
import com.guifroes1984.agendamentos.dto.resquest.ServicoRequest;
import com.guifroes1984.agendamentos.model.Servico;
import com.guifroes1984.agendamentos.service.ServicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicos")
@CrossOrigin(origins = "*")
@Tag(name = "Serviços", description = "Gerenciamento de serviços oferecidos pela manicure")
public class ServicoController {

	@Autowired
	private ServicoService servicoService;

	@Operation(summary = "Listar todos os serviços DTO")
	@ApiResponse(responseCode = "200", description = "Lista de serviços retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<ServicoResponse>> listarTodos() {
		List<ServicoResponse> servicos = servicoService.listarTodos();
		return ResponseEntity.ok(servicos);
	}

	@Operation(summary = "Listar serviços ativos DTO")
	@GetMapping("/ativos")
	public ResponseEntity<List<ServicoResponse>> listarAtivos() {
		List<ServicoResponse> servicos = servicoService.listarAtivos();
		return ResponseEntity.ok(servicos);
	}

	@Operation(summary = "Buscar serviço por ID DTO")
	@ApiResponse(responseCode = "200", description = "Serviço encontrado")
	@ApiResponse(responseCode = "404", description = "Serviço não encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<ServicoResponse> buscarPorId(@PathVariable Long id) {
		try {
			ServicoResponse servico = servicoService.buscarPorId(id);
			return ResponseEntity.ok(servico);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Cadastrar novo serviço", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = Servico.class))))
	@ApiResponse(responseCode = "201", description = "Serviço criado com sucesso DTO")
	@PostMapping
	public ResponseEntity<?> criar(@Valid @RequestBody ServicoRequest request) {
		try {
			ServicoResponse servico = servicoService.criar(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(servico);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Atualizar serviço existente DTO")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ServicoRequest request) {
		try {
			ServicoResponse servico = servicoService.atualizar(id, request);
			return ResponseEntity.ok(servico);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Excluir serviço (soft delete)")
	@ApiResponse(responseCode = "204", description = "Serviço desativado")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			servicoService.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Buscar serviços por categoria DTO")
	@GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<ServicoResponse>> buscarPorCategoria(@PathVariable String categoria) {
		try {
			List<ServicoResponse> servicos = servicoService.buscarPorCategoria(categoria);
			return ResponseEntity.ok(servicos);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Operation(summary = "Buscar serviços por nome DTO")
	@GetMapping("/buscar/{nome}")
	public ResponseEntity<List<ServicoResponse>> buscarPorNome(@PathVariable String nome) {
		List<ServicoResponse> servicos = servicoService.buscarPorNome(nome);
		return ResponseEntity.ok(servicos);
	}
}
