package com.guifroes1984.agendamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guifroes1984.agendamentos.dto.response.UsuarioResponse;
import com.guifroes1984.agendamentos.dto.resquest.UsuarioRequest;
import com.guifroes1984.agendamentos.model.Usuario;
import com.guifroes1984.agendamentos.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<UsuarioResponse> listarTodos() {
		return usuarioRepository.findAll().stream().map(UsuarioResponse::new).collect(Collectors.toList());
	}

	public UsuarioResponse buscarPorId(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
		return new UsuarioResponse(usuario);
	}

	public UsuarioResponse buscarPorEmail(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
		return new UsuarioResponse(usuario);
	}

	@Transactional
	public UsuarioResponse criar(UsuarioRequest request) {
		
		if (usuarioRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email já cadastrado: " + request.getEmail());
		}

		Usuario usuario = new Usuario();
		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());
		usuario.setSenha(passwordEncoder.encode(request.getSenha()));
		usuario.setRole(request.getRole() != null ? request.getRole() : Usuario.Role.CLIENTE);
		usuario.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

		Usuario salvo = usuarioRepository.save(usuario);
		return new UsuarioResponse(salvo);
	}

	@Transactional
	public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		if (!usuario.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email já cadastrado: " + request.getEmail());
		}

		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());
		if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
			usuario.setSenha(passwordEncoder.encode(request.getSenha()));
		}
		usuario.setRole(request.getRole());
		usuario.setAtivo(request.getAtivo());

		Usuario atualizado = usuarioRepository.save(usuario);
		return new UsuarioResponse(atualizado);
	}

	@Transactional
	public void deletar(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		usuario.setAtivo(false);
		usuarioRepository.save(usuario);
	}
}
