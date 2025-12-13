package com.guifroes1984.agendamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guifroes1984.agendamentos.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
	List<Servico> findByAtivoTrue();

	List<Servico> findByCategoria(Servico.CategoriaServico categoria);

	List<Servico> findByNomeContainingIgnoreCase(String nome);
}
