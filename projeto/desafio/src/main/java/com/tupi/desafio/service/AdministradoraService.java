package com.tupi.desafio.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.enums.StatusTransacao;

/**
 * Serviço responsável por implementar as regras de negócio da comunicação com a administradora
 */
@Service
public class AdministradoraService {
	/**
	 * Simula o envio da transação para a administradora e retorna o status
	 * 
	 * @param transacao A transação a ser enviada para a administradora do cartão
	 * @return StatusTransacao.APROVADA ou StatusTransacao.RECUSADA aleatoriamente
	 */
	public StatusTransacao enviarParaAdministradora(Transacao transacao){
		/* ~~ Lógica de envio para administradora e recuperação na resposta para saber se foi aprovada ou recusada ~~ */
		Random rd = new Random();
		return (rd.nextBoolean() ? StatusTransacao.APROVADA : StatusTransacao.RECUSADA);
	}
}
