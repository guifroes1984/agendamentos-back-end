package com.guifroes1984.agendamentos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guifroes1984.agendamentos.model.Profissional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
	List<Profissional> findByAtivoTrue();

	Optional<Profissional> findByUsuarioEmail(String email);

	Optional<Profissional> findByUsuarioId(Long usuarioId);

	List<Profissional> findByEspecialidadeContainingIgnoreCase(String especialidade);
}
