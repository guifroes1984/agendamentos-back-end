package com.guifroes1984.agendamentos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guifroes1984.agendamentos.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Optional<Cliente> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByTelefone(String telefone);
}
