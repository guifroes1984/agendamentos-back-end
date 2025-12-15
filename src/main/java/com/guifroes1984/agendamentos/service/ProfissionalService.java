package com.guifroes1984.agendamentos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }
    
    public List<Profissional> listarAtivos() {
        return profissionalRepository.findByAtivoTrue();
    }
    
    public Optional<Profissional> buscarPorId(Long id) {
        return profissionalRepository.findById(id);
    }
    
    public Optional<Profissional> buscarPorEmail(String email) {
        return profissionalRepository.findByUsuarioEmail(email);
    }
    
    @Transactional
    public Profissional criar(Profissional profissional) {
        // Validação básica
        if (profissional.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário é obrigatório para criar profissional");
        }
        
        // Se usuário tem ID, verificar se existe
        if (profissional.getUsuario().getId() != null) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(profissional.getUsuario().getId());
            if (usuarioExistente.isEmpty()) {
                throw new IllegalArgumentException("Usuário não encontrado com ID: " + profissional.getUsuario().getId());
            }
            
            // Atualizar role do usuário existente
            Usuario usuario = usuarioExistente.get();
            usuario.setRole(Usuario.Role.PROFISSIONAL);
            usuarioRepository.save(usuario);
        } else {
            // Se usuário não tem ID, salvar novo usuário
            Usuario novoUsuario = profissional.getUsuario();
            novoUsuario.setRole(Usuario.Role.PROFISSIONAL);
            Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
            profissional.setUsuario(usuarioSalvo);
        }
        
        return profissionalRepository.save(profissional);
    }
    
    @Transactional
    public Optional<Profissional> atualizar(Long id, Profissional profissionalAtualizado) {
        Optional<Profissional> profissionalExistente = profissionalRepository.findById(id);
        
        if (profissionalExistente.isEmpty()) {
            return Optional.empty();
        }
        
        Profissional profissional = profissionalExistente.get();
        
        // Atualizar campos permitidos
        profissional.setEspecialidade(profissionalAtualizado.getEspecialidade());
        profissional.setFotoUrl(profissionalAtualizado.getFotoUrl());
        profissional.setExperienciaAnos(profissionalAtualizado.getExperienciaAnos());
        profissional.setDiasTrabalho(profissionalAtualizado.getDiasTrabalho());
        profissional.setHoraInicio(profissionalAtualizado.getHoraInicio());
        profissional.setHoraFim(profissionalAtualizado.getHoraFim());
        profissional.setIntervaloMinutos(profissionalAtualizado.getIntervaloMinutos());
        profissional.setAtivo(profissionalAtualizado.getAtivo());
        
        return Optional.of(profissionalRepository.save(profissional));
    }
    
    @Transactional
    public boolean deletar(Long id) {
        Optional<Profissional> profissional = profissionalRepository.findById(id);
        
        if (profissional.isEmpty()) {
            return false;
        }
        
        // Soft delete - marca como inativo
        Profissional profissionalParaDeletar = profissional.get();
        profissionalParaDeletar.setAtivo(false);
        profissionalRepository.save(profissionalParaDeletar);
        
        return true;
    }
    
    public List<Profissional> buscarPorEspecialidade(String especialidade) {
        return profissionalRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }
}
