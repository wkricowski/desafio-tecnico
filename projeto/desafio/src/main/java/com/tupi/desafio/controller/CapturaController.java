package com.tupi.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.service.TransacaoService;

@RestController
@RequestMapping("/captura")
public class CapturaController {

	@Autowired
	private TransacaoService service;

	@PostMapping
	public ResponseEntity receber(@RequestBody RecebeTransacaoDTO dados){
		var list = service.processarTransacao(dados);

		return ResponseEntity.ok(list);
	}
}
