package com.guifroes1984.agendamentos.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guifroes1984.agendamentos.model.Servico;
import com.guifroes1984.agendamentos.repository.ServicoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/servicos")
@CrossOrigin(origins = "*")
@Tag(name = "Serviços", description = "CRUD de serviços oferecidos pela manicure")
public class ServicoController {

	@Autowired
	private ServicoRepository servicoRepository;

	@Operation(summary = "Listar todos os serviços")
	@ApiResponse(responseCode = "200", description = "Lista de serviços retornada com sucesso")
	@GetMapping
	public ResponseEntity<List<Servico>> listarTodos() {
		try {
			List<Servico> servicos = servicoRepository.findAll();
			return ResponseEntity.ok(servicos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Listar serviços ativos")
	@GetMapping("/ativos")
	public ResponseEntity<List<Servico>> listarAtivos() {
		try {
			List<Servico> servicos = servicoRepository.findByAtivoTrue();
			return ResponseEntity.ok(servicos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar serviço por ID")
	@ApiResponse(responseCode = "200", description = "Serviço encontrado")
	@ApiResponse(responseCode = "404", description = "Serviço não encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
		try {
			Optional<Servico> servico = servicoRepository.findById(id);

			if (servico.isPresent()) {
				return ResponseEntity.ok(servico.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Cadastrar novo serviço", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = Servico.class))))
	@ApiResponse(responseCode = "201", description = "Serviço criado com sucesso")
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Servico servico) {
		try {

			if (servico.getNome() == null || servico.getNome().trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Nome é obrigatório");
			}

			if (servico.getDuracaoMinutos() == null || servico.getDuracaoMinutos() <= 0) {
				return ResponseEntity.badRequest().body("Duração deve ser maior que zero");
			}

			if (servico.getPreco() == null || servico.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
				return ResponseEntity.badRequest().body("Preço deve ser maior que zero");
			}

			if (servico.getAtivo() == null) {
				servico.setAtivo(true);
			}

			Servico servicoSalvo = servicoRepository.save(servico);
			return ResponseEntity.status(HttpStatus.CREATED).body(servicoSalvo);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao criar serviço: " + e.getMessage());
		}
	}

	@Operation(summary = "Atualizar serviço existente")
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Servico servicoAtualizado) {
		try {
			Optional<Servico> servicoExistente = servicoRepository.findById(id);

			if (servicoExistente.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Servico servico = servicoExistente.get();

			servico.setNome(servicoAtualizado.getNome());
			servico.setDescricao(servicoAtualizado.getDescricao());
			servico.setDuracaoMinutos(servicoAtualizado.getDuracaoMinutos());
			servico.setPreco(servicoAtualizado.getPreco());
			servico.setCategoria(servicoAtualizado.getCategoria());
			servico.setAtivo(servicoAtualizado.getAtivo());

			Servico servicoAtualizadoSalvo = servicoRepository.save(servico);
			return ResponseEntity.ok(servicoAtualizadoSalvo);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao atualizar serviço: " + e.getMessage());
		}
	}

	@Operation(summary = "Excluir serviço (soft delete)")
	@ApiResponse(responseCode = "204", description = "Serviço desativado")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		try {
			Optional<Servico> servico = servicoRepository.findById(id);

			if (servico.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			Servico servicoParaDeletar = servico.get();
			servicoParaDeletar.setAtivo(false);
			servicoRepository.save(servicoParaDeletar);

			return ResponseEntity.noContent().build();

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar serviços por categoria")
	@GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<Servico>> buscarPorCategoria(@PathVariable String categoria) {
		try {
			Servico.CategoriaServico categoriaEnum = Servico.CategoriaServico.valueOf(categoria.toUpperCase());
			List<Servico> servicos = servicoRepository.findByCategoria(categoriaEnum);
			return ResponseEntity.ok(servicos);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Operation(summary = "Buscar serviços por nome")
	@GetMapping("/buscar/{nome}")
	public ResponseEntity<List<Servico>> buscarPorNome(@PathVariable String nome) {
		try {
			List<Servico> servicos = servicoRepository.findByNomeContainingIgnoreCase(nome);
			return ResponseEntity.ok(servicos);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
