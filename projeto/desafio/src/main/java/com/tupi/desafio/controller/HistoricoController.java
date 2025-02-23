package com.tupi.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.service.HistoricoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Essa classe é responsavel pelos endpoints de consulta, ela irá realizar a busca por transações processadas no sistema.
 */
@RestController
@RequestMapping("/historico")
@Tag(name = "Consultas de Histórico", description = "Endpoints para acessar o histórico de recebimento de transações")
public class HistoricoController {
	@Autowired
	private HistoricoService service;


	/* LISTAR O HISTÓRICO COMPLETO COM PAGINAÇÃO */
	@Operation(
		summary = "Consulta todo o histórico de transações",
		description = "Irá listar todas as transações presentes no banco de dados e aplicar suporte a paginação"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Requisição processada com sucesso"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	@GetMapping
	public ResponseEntity<Page<Transacao>> listarTudo(@PageableDefault(size = 20, sort = {"id"}, direction = Direction.DESC) Pageable paginacao){
		var page = service.listarTodasTransacoes(paginacao);
		return ResponseEntity.ok(page);
	}


	/* RECUPERAR TRANSAÇÃO COM BASE EM UM ID ESPECÍFICO */
	@Operation(
		summary = "Consultar uma transação em específico",
		description = "Irá recuperar uma transação em específico com base no id."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Requisição processada com sucesso"),
		@ApiResponse(responseCode = "204", description = "Registro não encontrado"),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	})
	@GetMapping("/{id}")
	public ResponseEntity buscarPorId(@PathVariable Integer id){
		var list = service.buscarTransacao(id);
		return (list.size() == 0) ? ResponseEntity.notFound().build() : ResponseEntity.ok(list);
	}
}
