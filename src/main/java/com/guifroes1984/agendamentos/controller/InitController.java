package com.guifroes1984.agendamentos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.agendamentos.config.JwtService;
import com.guifroes1984.agendamentos.model.Usuario;
import com.guifroes1984.agendamentos.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/init")
@CrossOrigin(origins = "*")
@Tag(name = "Inicialização", description = "Endpoints para inicialização do sistema")
public class InitController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/create-admin")
	@Operation(summary = "Criar administrador inicial")
	public ResponseEntity<?> createAdmin(@RequestParam String email, @RequestParam String senha) {

		try {
			// Criar admin
			Usuario admin = authService.criarAdminInicial(email, senha);

			// Gerar token
			String token = jwtService.generateToken(admin.getEmail());

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Administrador criado com sucesso!");
			response.put("data", Map.of("id", admin.getId(), "nome", admin.getNome(), "email", admin.getEmail(), "role",
					admin.getRole()));
			response.put("token", token);

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (RuntimeException e) {
			Map<String, Object> error = new HashMap<>();
			error.put("success", false);
			error.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(error);
		}
	}

	@GetMapping("/status")
	@Operation(summary = "Verificar status da inicialização")
	public ResponseEntity<Map<String, String>> getStatus() {
		Map<String, String> response = new HashMap<>();
		response.put("status", "ready");
		response.put("message", "Sistema pronto para inicialização");
		response.put("endpoint", "POST /api/init/create-admin?email=admin@email.com&senha=senha123");
		return ResponseEntity.ok(response);
	}
}
