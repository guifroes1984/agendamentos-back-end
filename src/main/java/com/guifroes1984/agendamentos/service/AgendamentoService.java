package com.guifroes1984.agendamentos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guifroes1984.agendamentos.dto.response.AgendamentoResponse;
import com.guifroes1984.agendamentos.dto.resquest.AgendamentoRequest;
import com.guifroes1984.agendamentos.model.Agendamento;
import com.guifroes1984.agendamentos.model.Cliente;
import com.guifroes1984.agendamentos.model.Profissional;
import com.guifroes1984.agendamentos.model.Servico;
import com.guifroes1984.agendamentos.repository.AgendamentoRepository;
import com.guifroes1984.agendamentos.repository.ClienteRepository;
import com.guifroes1984.agendamentos.repository.ProfissionalRepository;
import com.guifroes1984.agendamentos.repository.ServicoRepository;

@Service
public class AgendamentoService {

	@Autowired
	private AgendamentoRepository agendamentoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ProfissionalRepository profissionalRepository;

	@Autowired
	private ServicoRepository servicoRepository;

	public List<Agendamento> listarTodos() {
		return agendamentoRepository.findAll();
	}

	public Optional<Agendamento> buscarPorId(Long id) {
		return agendamentoRepository.findById(id);
	}

	public List<Agendamento> buscarPorCliente(Long clienteId) {
		return agendamentoRepository.findByClienteId(clienteId);
	}

	public List<Agendamento> buscarFuturosPorCliente(Long clienteId) {
		return agendamentoRepository.findFuturosByClienteId(clienteId, LocalDateTime.now());
	}

	public List<Agendamento> buscarPassadosPorCliente(Long clienteId) {
		return agendamentoRepository.findPassadosByClienteId(clienteId, LocalDateTime.now());
	}

	public List<Agendamento> buscarPorProfissional(Long profissionalId) {
		return agendamentoRepository.findByProfissionalId(profissionalId);
	}

	public List<Agendamento> buscarFuturosPorProfissional(Long profissionalId) {
		return agendamentoRepository.findFuturosByProfissionalId(profissionalId, LocalDateTime.now());
	}

	public List<Agendamento> buscarPorStatus(String statusStr) {
		try {
			Agendamento.StatusAgendamento status = Agendamento.StatusAgendamento.valueOf(statusStr.toUpperCase());
			return agendamentoRepository.findByStatus(status);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Status inválido: " + statusStr);
		}
	}

	@Transactional
	public Agendamento criar(Agendamento agendamento) {
		validarAgendamento(agendamento);

		Cliente cliente = clienteRepository.findById(agendamento.getCliente().getId())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

		Profissional profissional = profissionalRepository.findById(agendamento.getProfissional().getId())
				.orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));

		Servico servico = servicoRepository.findById(agendamento.getServico().getId())
				.orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

		agendamento.setCliente(cliente);
		agendamento.setProfissional(profissional);
		agendamento.setServico(servico);

		agendamento.calcularDataHoraFim();

		if (!profissionalDisponivel(profissional, agendamento.getDataHoraInicio())) {
			throw new IllegalArgumentException("Profissional não trabalha neste dia/horário");
		}

		if (existeConflitoHorario(agendamento)) {
			throw new IllegalArgumentException("Horário já ocupado para este profissional");
		}

		agendamento.setStatus(Agendamento.StatusAgendamento.AGENDADO);
		agendamento.setDataCriacao(LocalDateTime.now());
		agendamento.setDataAtualizacao(LocalDateTime.now());

		return agendamentoRepository.save(agendamento);
	}

	@Transactional
	public AgendamentoResponse criar(AgendamentoRequest request) {

		Cliente cliente = clienteRepository.findById(request.getClienteId())
				.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

		Profissional profissional = profissionalRepository.findById(request.getProfissionalId())
				.orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));

		Servico servico = servicoRepository.findById(request.getServicoId())
				.orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

		Agendamento agendamento = new Agendamento();
		agendamento.setCliente(cliente);
		agendamento.setProfissional(profissional);
		agendamento.setServico(servico);
		agendamento.setDataHoraInicio(request.getDataHoraInicio());
		agendamento.setObservacoes(request.getObservacoes());

		Agendamento agendamentoSalvo = criar(agendamento);

		return new AgendamentoResponse(agendamentoSalvo);
	}

	public List<AgendamentoResponse> listarTodosResponse() {
		return agendamentoRepository.findAll().stream().map(AgendamentoResponse::new).collect(Collectors.toList());
	}

	public AgendamentoResponse buscarPorIdResponse(Long id) {
		Agendamento agendamento = agendamentoRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));
		return new AgendamentoResponse(agendamento);
	}

	public List<AgendamentoResponse> buscarPorClienteResponse(Long clienteId) {
		return agendamentoRepository.findByClienteId(clienteId).stream().map(AgendamentoResponse::new)
				.collect(Collectors.toList());
	}

	public List<AgendamentoResponse> buscarFuturosPorClienteResponse(Long clienteId) {
		return agendamentoRepository.findFuturosByClienteId(clienteId, LocalDateTime.now()).stream()
				.map(AgendamentoResponse::new).collect(Collectors.toList());
	}

	public List<AgendamentoResponse> buscarPassadosPorClienteResponse(Long clienteId) {
		return agendamentoRepository.findPassadosByClienteId(clienteId, LocalDateTime.now()).stream()
				.map(AgendamentoResponse::new).collect(Collectors.toList());
	}

	public List<AgendamentoResponse> buscarPorProfissionalResponse(Long profissionalId) {
		return agendamentoRepository.findByProfissionalId(profissionalId).stream().map(AgendamentoResponse::new)
				.collect(Collectors.toList());
	}

	public List<AgendamentoResponse> buscarFuturosPorProfissionalResponse(Long profissionalId) {
		return agendamentoRepository.findFuturosByProfissionalId(profissionalId, LocalDateTime.now()).stream()
				.map(AgendamentoResponse::new).collect(Collectors.toList());
	}

	public List<AgendamentoResponse> buscarPorStatusResponse(String statusStr) {
		try {
			Agendamento.StatusAgendamento status = Agendamento.StatusAgendamento.valueOf(statusStr.toUpperCase());
			return agendamentoRepository.findByStatus(status).stream().map(AgendamentoResponse::new)
					.collect(Collectors.toList());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Status inválido: " + statusStr);
		}
	}

	public List<AgendamentoResponse> buscarPorPeriodoResponse(LocalDateTime inicio, LocalDateTime fim) {
		return agendamentoRepository.findByDataHoraInicioBetween(inicio, fim).stream().map(AgendamentoResponse::new)
				.collect(Collectors.toList());
	}

	@Transactional
	public AgendamentoResponse atualizarStatusResponse(Long id, String statusStr) {
		Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);

		if (agendamentoOpt.isEmpty()) {
			throw new IllegalArgumentException("Agendamento não encontrado");
		}

		try {
			Agendamento.StatusAgendamento novoStatus = Agendamento.StatusAgendamento.valueOf(statusStr.toUpperCase());
			Agendamento agendamento = agendamentoOpt.get();
			agendamento.setStatus(novoStatus);
			agendamento.setDataAtualizacao(LocalDateTime.now());

			Agendamento atualizado = agendamentoRepository.save(agendamento);
			return new AgendamentoResponse(atualizado);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Status inválido: " + statusStr);
		}
	}

	@Transactional
	public AgendamentoResponse cancelarResponse(Long id) {
		Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);

		if (agendamentoOpt.isEmpty()) {
			throw new IllegalArgumentException("Agendamento não encontrado");
		}

		Agendamento agendamento = agendamentoOpt.get();

		if (agendamento.getStatus() == Agendamento.StatusAgendamento.REALIZADO) {
			throw new IllegalArgumentException("Não é possível cancelar um agendamento já realizado");
		}

		if (agendamento.getDataHoraInicio().minusHours(2).isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Agendamentos só podem ser cancelados com 2 horas de antecedência");
		}

		agendamento.setStatus(Agendamento.StatusAgendamento.CANCELADO);
		agendamento.setDataAtualizacao(LocalDateTime.now());

		Agendamento cancelado = agendamentoRepository.save(agendamento);
		return new AgendamentoResponse(cancelado);
	}

	public AgendamentoResponse toResponse(Agendamento agendamento) {
		return new AgendamentoResponse(agendamento);
	}

	@Transactional
	public Optional<Agendamento> atualizar(Long id, Agendamento agendamentoAtualizado) {
		Optional<Agendamento> agendamentoExistente = agendamentoRepository.findById(id);

		if (agendamentoExistente.isEmpty()) {
			return Optional.empty();
		}

		Agendamento agendamento = agendamentoExistente.get();

		if (agendamento.getStatus() == Agendamento.StatusAgendamento.CANCELADO
				|| agendamento.getStatus() == Agendamento.StatusAgendamento.REALIZADO) {
			throw new IllegalArgumentException("Não é possível atualizar um agendamento " + agendamento.getStatus());
		}

		if (agendamentoAtualizado.getObservacoes() != null) {
			agendamento.setObservacoes(agendamentoAtualizado.getObservacoes());
		}

		if (agendamentoAtualizado.getStatus() != null) {
			agendamento.setStatus(agendamentoAtualizado.getStatus());
		}

		agendamento.setDataAtualizacao(LocalDateTime.now());

		return Optional.of(agendamentoRepository.save(agendamento));
	}

	@Transactional
	public Optional<Agendamento> atualizarStatus(Long id, String statusStr) {
		Optional<Agendamento> agendamento = agendamentoRepository.findById(id);

		if (agendamento.isEmpty()) {
			return Optional.empty();
		}

		try {
			Agendamento.StatusAgendamento novoStatus = Agendamento.StatusAgendamento.valueOf(statusStr.toUpperCase());
			Agendamento agendamentoAtual = agendamento.get();
			agendamentoAtual.setStatus(novoStatus);
			agendamentoAtual.setDataAtualizacao(LocalDateTime.now());

			return Optional.of(agendamentoRepository.save(agendamentoAtual));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Status inválido: " + statusStr);
		}
	}

	@Transactional
	public boolean cancelar(Long id) {
		Optional<Agendamento> agendamento = agendamentoRepository.findById(id);

		if (agendamento.isEmpty()) {
			return false;
		}

		Agendamento agendamentoAtual = agendamento.get();

		if (agendamentoAtual.getStatus() == Agendamento.StatusAgendamento.REALIZADO) {
			throw new IllegalArgumentException("Não é possível cancelar um agendamento já realizado");
		}

		if (agendamentoAtual.getDataHoraInicio().minusHours(2).isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Agendamentos só podem ser cancelados com 2 horas de antecedência");
		}

		agendamentoAtual.setStatus(Agendamento.StatusAgendamento.CANCELADO);
		agendamentoAtual.setDataAtualizacao(LocalDateTime.now());
		agendamentoRepository.save(agendamentoAtual);

		return true;
	}

	public boolean verificarDisponibilidade(Long profissionalId, LocalDateTime inicio, LocalDateTime fim) {
		return !agendamentoRepository.existeConflitoHorario(profissionalId, inicio, fim);
	}

	public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
		return agendamentoRepository.findByDataHoraInicioBetween(inicio, fim);
	}

	private void validarAgendamento(Agendamento agendamento) {
		if (agendamento.getCliente() == null || agendamento.getCliente().getId() == null) {
			throw new IllegalArgumentException("Cliente é obrigatório");
		}

		if (agendamento.getProfissional() == null || agendamento.getProfissional().getId() == null) {
			throw new IllegalArgumentException("Profissional é obrigatório");
		}

		if (agendamento.getServico() == null || agendamento.getServico().getId() == null) {
			throw new IllegalArgumentException("Serviço é obrigatório");
		}

		if (agendamento.getDataHoraInicio() == null) {
			throw new IllegalArgumentException("Data e hora são obrigatórias");
		}

		if (agendamento.getDataHoraInicio().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Não é possível agendar para datas passadas");
		}

		if (!clienteRepository.existsById(agendamento.getCliente().getId())) {
			throw new IllegalArgumentException("Cliente não encontrado");
		}

		if (!profissionalRepository.existsById(agendamento.getProfissional().getId())) {
			throw new IllegalArgumentException("Profissional não encontrado");
		}

		if (!servicoRepository.existsById(agendamento.getServico().getId())) {
			throw new IllegalArgumentException("Serviço não encontrado");
		}
	}

	private boolean existeConflitoHorario(Agendamento agendamento) {
		if (agendamento.getDataHoraFim() == null) {
			agendamento.calcularDataHoraFim();
		}

		return agendamentoRepository.existeConflitoHorario(agendamento.getProfissional().getId(),
				agendamento.getDataHoraInicio(), agendamento.getDataHoraFim());
	}

	private boolean profissionalDisponivel(Profissional profissional, LocalDateTime dataHora) {

		if (!profissional.getDiasTrabalho().contains(converterParaDiaSemana(dataHora))) {
			return false;
		}

		LocalDateTime inicioExpediente = dataHora.toLocalDate().atTime(profissional.getHoraInicio());
		LocalDateTime fimExpediente = dataHora.toLocalDate().atTime(profissional.getHoraFim());

		return !dataHora.isBefore(inicioExpediente) && !dataHora.isAfter(fimExpediente);
	}

	private Profissional.DiaSemana converterParaDiaSemana(LocalDateTime dataHora) {
		switch (dataHora.getDayOfWeek()) {
		case MONDAY:
			return Profissional.DiaSemana.SEGUNDA;
		case TUESDAY:
			return Profissional.DiaSemana.TERCA;
		case WEDNESDAY:
			return Profissional.DiaSemana.QUARTA;
		case THURSDAY:
			return Profissional.DiaSemana.QUINTA;
		case FRIDAY:
			return Profissional.DiaSemana.SEXTA;
		case SATURDAY:
			return Profissional.DiaSemana.SABADO;
		case SUNDAY:
			return Profissional.DiaSemana.DOMINGO;
		default:
			throw new IllegalArgumentException("Dia da semana inválido");
		}
	}
}