package com.guifroes1984.agendamentos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guifroes1984.agendamentos.config.JwtService;
import com.guifroes1984.agendamentos.dto.response.AuthResponse;
import com.guifroes1984.agendamentos.dto.response.RegisterRequest;
import com.guifroes1984.agendamentos.dto.resquest.AuthRequest;
import com.guifroes1984.agendamentos.model.Usuario;
import com.guifroes1984.agendamentos.repository.UsuarioRepository;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		// Verificar se email já existe
		if (usuarioRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email já cadastrado: " + request.getEmail());
		}

		// Criar novo usuário
		Usuario usuario = Usuario.builder().nome(request.getNome()).email(request.getEmail())
				.senha(passwordEncoder.encode(request.getSenha())).role(request.getRole()).ativo(true).build();

		Usuario savedUsuario = usuarioRepository.save(usuario);

		// Gerar token JWT
		String jwtToken = jwtService.generateToken(usuario.getEmail());

		return new AuthResponse(jwtToken, savedUsuario.getId(), savedUsuario.getNome(), savedUsuario.getEmail(),
				savedUsuario.getRole(), savedUsuario.getAtivo());
	}

	@Transactional(readOnly = true)
	public AuthResponse authenticate(AuthRequest request) {
		// Autenticar usuário
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

		// Buscar usuário
		Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		if (!usuario.getAtivo()) {
			throw new RuntimeException("Conta inativa");
		}

		// Gerar token JWT
		String jwtToken = jwtService.generateToken(usuario.getEmail());

		return new AuthResponse(jwtToken, usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole(),
				usuario.getAtivo());
	}

	@Transactional
	public Usuario criarAdminInicial(String email, String senha) {
		// Verificar se já existe admin
		if (usuarioRepository.existsByEmail(email)) {
			throw new RuntimeException("Admin já existe com este email");
		}

		// Criar admin
		Usuario admin = Usuario.builder().nome("Administrador").email(email).senha(passwordEncoder.encode(senha))
				.role(Usuario.Role.ADMIN).ativo(true).build();

		return usuarioRepository.save(admin);
	}
}
