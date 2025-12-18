package com.guifroes1984.agendamentos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guifroes1984.agendamentos.dto.response.ProfissionalResponse;
import com.guifroes1984.agendamentos.dto.resquest.ProfissionalRequest;
import com.guifroes1984.agendamentos.model.Profissional;
import com.guifroes1984.agendamentos.model.Usuario;
import com.guifroes1984.agendamentos.repository.ProfissionalRepository;
import com.guifroes1984.agendamentos.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ProfissionalService {

	@Autowired
	private ProfissionalRepository profissionalRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<ProfissionalResponse> listarTodos() {
		return profissionalRepository.findAll().stream().map(ProfissionalResponse::new).collect(Collectors.toList());
	}

	public List<ProfissionalResponse> listarAtivos() {
		return profissionalRepository.findByAtivoTrue().stream().map(ProfissionalResponse::new)
				.collect(Collectors.toList());
	}

	public ProfissionalResponse buscarPorId(Long id) {
		Profissional profissional = profissionalRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
		return new ProfissionalResponse(profissional);
	}

	public ProfissionalResponse buscarPorEmail(String email) {
		Profissional profissional = profissionalRepository.findByUsuarioEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
		return new ProfissionalResponse(profissional);
	}

	@Transactional
	public ProfissionalResponse criar(ProfissionalRequest request) {

		Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		if (profissionalRepository.findByUsuarioId(usuario.getId()).isPresent()) {
			throw new IllegalArgumentException("Já existe profissional cadastrado para este usuário");
		}

		usuario.setRole(Usuario.Role.PROFISSIONAL);
		usuarioRepository.save(usuario);

		Profissional profissional = new Profissional();
		profissional.setUsuario(usuario);
		profissional.setEspecialidade(request.getEspecialidade());
		profissional.setFotoUrl(request.getFotoUrl());
		profissional.setExperienciaAnos(request.getExperienciaAnos());
		profissional.setDiasTrabalho(request.getDiasTrabalho());
		profissional.setHoraInicio(request.getHoraInicio());
		profissional.setHoraFim(request.getHoraFim());
		profissional.setIntervaloMinutos(request.getIntervaloMinutos() != null ? request.getIntervaloMinutos() : 15);
		profissional.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

		Profissional salvo = profissionalRepository.save(profissional);
		return new ProfissionalResponse(salvo);
	}

	@Transactional
	public ProfissionalResponse atualizar(Long id, ProfissionalRequest request) {
		Profissional profissional = profissionalRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));

		profissional.setEspecialidade(request.getEspecialidade());
		profissional.setFotoUrl(request.getFotoUrl());
		profissional.setExperienciaAnos(request.getExperienciaAnos());
		profissional.setDiasTrabalho(request.getDiasTrabalho());
		profissional.setHoraInicio(request.getHoraInicio());
		profissional.setHoraFim(request.getHoraFim());
		profissional.setIntervaloMinutos(request.getIntervaloMinutos());
		profissional.setAtivo(request.getAtivo());

		Profissional atualizado = profissionalRepository.save(profissional);
		return new ProfissionalResponse(atualizado);
	}

	@Transactional
	public void deletar(Long id) {
		Profissional profissional = profissionalRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));

		profissional.setAtivo(false);
		profissionalRepository.save(profissional);
	}

	public List<ProfissionalResponse> buscarPorEspecialidade(String especialidade) {
		return profissionalRepository.findByEspecialidadeContainingIgnoreCase(especialidade).stream()
				.map(ProfissionalResponse::new).collect(Collectors.toList());
	}
}
