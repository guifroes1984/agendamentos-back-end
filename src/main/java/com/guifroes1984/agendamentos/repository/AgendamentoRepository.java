package com.guifroes1984.agendamentos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guifroes1984.agendamentos.model.Agendamento;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

	List<Agendamento> findByClienteId(Long clienteId);

	List<Agendamento> findByProfissionalId(Long profissionalId);

	List<Agendamento> findByStatus(Agendamento.StatusAgendamento status);

	@Query("SELECT a FROM Agendamento a WHERE a.cliente.id = :clienteId AND a.dataHoraInicio >= :dataAtual ORDER BY a.dataHoraInicio ASC")
	List<Agendamento> findFuturosByClienteId(@Param("clienteId") Long clienteId,
			@Param("dataAtual") LocalDateTime dataAtual);

	@Query("SELECT a FROM Agendamento a WHERE a.cliente.id = :clienteId AND a.dataHoraInicio < :dataAtual ORDER BY a.dataHoraInicio DESC")
	List<Agendamento> findPassadosByClienteId(@Param("clienteId") Long clienteId,
			@Param("dataAtual") LocalDateTime dataAtual);

	@Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataHoraInicio >= :dataAtual ORDER BY a.dataHoraInicio ASC")
	List<Agendamento> findFuturosByProfissionalId(@Param("profissionalId") Long profissionalId,
			@Param("dataAtual") LocalDateTime dataAtual);

	@Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE " + "a.profissional.id = :profissionalId AND "
			+ "a.status <> 'CANCELADO' AND " + "a.status <> 'FALTOU' AND "
			+ "((a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio))")
	boolean existeConflitoHorario(@Param("profissionalId") Long profissionalId, @Param("inicio") LocalDateTime inicio,
			@Param("fim") LocalDateTime fim);

	List<Agendamento> findByDataHoraInicioBetween(LocalDateTime inicio, LocalDateTime fim);

	List<Agendamento> findByProfissionalIdAndDataHoraInicioBetween(Long profissionalId, LocalDateTime inicio,
			LocalDateTime fim);
}
