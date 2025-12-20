package com.guifroes1984.agendamentos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.agendamentos.dto.response.AuthResponse;
import com.guifroes1984.agendamentos.dto.response.RegisterRequest;
import com.guifroes1984.agendamentos.dto.resquest.AuthRequest;
import com.guifroes1984.agendamentos.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	@Operation(summary = "Login de usuário")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
		AuthResponse response = authService.authenticate(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	@Operation(summary = "Registro de novo usuário")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		AuthResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	@Operation(summary = "Obter informações do usuário atual")
	public ResponseEntity<String> getCurrentUser() {
		return ResponseEntity.ok("Usuário autenticado com sucesso!");
	}

	@GetMapping("/health")
	@Operation(summary = "Verificar saúde do serviço de autenticação")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("Auth service is healthy!");
	}
}
