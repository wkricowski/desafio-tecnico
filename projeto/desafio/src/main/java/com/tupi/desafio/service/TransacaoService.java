package com.tupi.desafio.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.interfaces.ValidacoesTLV;
import com.tupi.desafio.mapper.TransacaoMapper;

@Service
public class TransacaoService {

	@Autowired
	private List<ValidacoesTLV> validadores;

	@Autowired
	private AdministradoraService administradoraService;


	/**
	 * Processa uma transação com base nos dados recebidos
	 * 
	 * @param dados DTO contendo os dados EMV
	 * @return A transação processada com status e NSU gerado
	 */
	public Transacao processarTransacao(RecebeTransacaoDTO dados) {
		var transacao = TransacaoMapper.fromDTO(dados);

		// Percorre a lista de validações que implementam a 'interface/ValidacoesTLV.java'
		validadores.forEach(v -> v.validar(transacao));

		// Se chegou aqui, estamos com uma transação válida, então vamos gerar um NSU para ela
		transacao.setNsu(gerarNSU());

		// Envia para a administradora do cartão e salva o status/retorno
		var retorno = administradoraService.enviarParaAdministradora(transacao);
		transacao.setStatus(retorno);

		return transacao;
	}

	/**
	 * Gera um NSU (Número Sequencial Único) para a transação
	 *
	 * @return Um número inteiro aleatório entre 0 e 999999
	 */
	private int gerarNSU() {
		// gera um valor aleatório para utilizarmos como NSU pro desafio
		return ThreadLocalRandom.current().nextInt(999999);
	}
}
