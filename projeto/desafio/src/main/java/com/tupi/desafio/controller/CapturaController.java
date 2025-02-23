package com.tupi.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tupi.desafio.dto.ErroDTO;
import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.dto.RespondeTransacaoDTO;
import com.tupi.desafio.service.TransacaoService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;

/**
 * Essa classe é responsavel pelo endpoint de captura, onde irá aguardar o envio
 * de dados EMV (via Tags TLV) capturados do cartão e iniciar o processamento
 */

@RestController
@RequestMapping("/captura")
@Tag(name = "Captura de Transações", description = "Endpoints para receber e processar as transações")
public class CapturaController {

	@Autowired
	private TransacaoService service;

	/* PROCESSAR DADOS/TAGS EMV */
	@PostMapping
	@Transactional
	@Operation(
		summary = "Processa uma transação financeira",
		description = "Recebe os dados EMV capturados do cartão para iniciar o processamento da transação."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Transação processada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RespondeTransacaoDTO.class))),
		@ApiResponse(responseCode = "400", description = "Dados enviados estão inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  ErroDTO.class))),
		@ApiResponse(responseCode = "500", description = "Erro interno no servidor",content = @Content(mediaType = "application/json", schema = @Schema(implementation =  ErroDTO.class)))
	})
	public ResponseEntity receber(@RequestBody RecebeTransacaoDTO dados, UriComponentsBuilder uriBuilder){
		var transacao = service.processarTransacao(dados);

		// cria o uri para o location no header
		var uri = uriBuilder.path("/historico/{id}").buildAndExpand(transacao.getId()).toUri();
		return ResponseEntity.created(uri).body(new RespondeTransacaoDTO(transacao));
	}
}
