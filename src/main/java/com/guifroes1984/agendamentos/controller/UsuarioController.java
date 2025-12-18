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

import com.guifroes1984.agendamentos.dto.response.UsuarioResponse;
import com.guifroes1984.agendamentos.dto.resquest.UsuarioRequest;
import com.guifroes1984.agendamentos.model.Usuario;
import com.guifroes1984.agendamentos.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Listar todos os usuários DTO", description = "Retorna uma lista com todos os usuários cadastrados")
	@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<UsuarioResponse>> listarTodos() {
		List<UsuarioResponse> usuarios = usuarioService.listarTodos();
		return ResponseEntity.ok(usuarios);
	}

	@Operation(summary = "Buscar usuário por ID DTO", description = "Retorna os dados de um usuário com base no ID informado")
	@ApiResponse(responseCode = "200", description = "Usuário encontrado")
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
		try {
			UsuarioResponse usuario = usuarioService.buscarPorId(id);
			return ResponseEntity.ok(usuario);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Buscar usuário por email DTO", description = "Retorna os dados de um usuário com base no email informado")
	@ApiResponse(responseCode = "200", description = "Usuário encontrado")
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	@GetMapping("/email/{email}")
	public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
		try {
			UsuarioResponse usuario = usuarioService.buscarPorEmail(email);
			return ResponseEntity.ok(usuario);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Cadastrar novo usuário DTO", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = Usuario.class))))
	@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	@ApiResponse(responseCode = "409", description = "Email já cadastrado")
	@PostMapping
	public ResponseEntity<?> criar(@Valid @RequestBody UsuarioRequest request) {
		try {
			UsuarioResponse usuario = usuarioService.criar(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Atualizar usuário DTO", description = "Atualiza os dados de um usuário existente")
	@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
		try {
			UsuarioResponse usuario = usuarioService.atualizar(id, request);
			return ResponseEntity.ok(usuario);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema pelo ID")
	@ApiResponse(responseCode = "204", description = "Usuário removido com sucesso")
	@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			usuarioService.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
