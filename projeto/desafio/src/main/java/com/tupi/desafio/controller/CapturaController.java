package com.tupi.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.service.TransacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/captura")
@Tag(name = "Captura de Transações", description = "Endpoints para receber e processar as transações")
public class CapturaController {

	@Autowired
	private TransacaoService service;

	@PostMapping
	@Operation(
		summary = "Processa uma transação financeira",
		description = "Recebe os dados EMV capturados do cartão para iniciar o processamento da transação."
	)
	public ResponseEntity receber(@RequestBody RecebeTransacaoDTO dados){
		var transacao = service.processarTransacao(dados);

		return ResponseEntity.ok(transacao);
	}
}
