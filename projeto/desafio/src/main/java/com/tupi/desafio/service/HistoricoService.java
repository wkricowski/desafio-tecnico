package com.tupi.desafio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.repository.TransacaoRepository;

/**
 * Serviço responsável por implementar as regras de negócio relacionadas ao histórico de transações
 */

@Service
public class HistoricoService {

	@Autowired
	private TransacaoRepository repository;

	/**
	 * Busca uma transacao em especifica com base no id
	 * 
	 * @param id identificador de uma transacao 
	 * @return Lista contendo um unico objeto Transacao, ou uma lista vazia caso o id nao seja encontrado
	 */
	public List<Transacao> buscarTransacao(Integer id) {
		return repository.findById(id).stream().toList();
	}


	/**
	 * Busca todas as transações registradas
	 * 
	 * @param paginacao que define os parâmetros de paginação.
	 * @return Página de Transacao contendo os registros encontrados.
	 */
	public Page<Transacao> listarTodasTransacoes(Pageable paginacao){
		return repository.findAll(paginacao);
	}	
}
