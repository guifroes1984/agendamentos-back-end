package com.guifroes1984.agendamentos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.agendamentos.model.Usuario;
import com.guifroes1984.agendamentos.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Operation(summary = "Listar todos os usuários")
	@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<Usuario>> listarTodos() {
		List<Usuario> usuarios = usuarioRepository.findAll();
		return ResponseEntity.ok(usuarios);
	}

	@Operation(summary = "Cadastrar novo usuário", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = Usuario.class))))
	@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
	@ApiResponse(responseCode = "400", description = "Dados inválidos")
	@ApiResponse(responseCode = "409", description = "Email já cadastrado")
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Usuario usuario) {
		try {

			if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Email é obrigatório");
			}

			if (usuarioRepository.existsByEmail(usuario.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado: " + usuario.getEmail());
			}

			if (usuario.getRole() == null) {
				usuario.setRole(Usuario.Role.CLIENTE);
			}

			if (usuario.getAtivo() == null) {
				usuario.setAtivo(true);
			}

			Usuario usuarioSalvo = usuarioRepository.save(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar usuário: " + e.getMessage());
		}
	}
}
