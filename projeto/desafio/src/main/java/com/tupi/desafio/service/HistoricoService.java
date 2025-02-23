package com.tupi.desafio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tupi.desafio.dto.RespondeTransacaoDTO;
import com.tupi.desafio.mapper.TransacaoMapper;
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
	 * @return Lista contendo um unico RespondeTransacaoDTO, ou uma lista vazia caso o id não seja encontrado
	 */
	public List<RespondeTransacaoDTO> buscarTransacao(Integer id) {
		return repository.findById(id).stream().map(TransacaoMapper::toDTO).toList();
	}


	/**
	 * Busca todas as transações registradas
	 * 
	 * @param paginacao que define os parâmetros de paginação.
	 * @return Página de RespondeTransacaoDTO contendo os registros encontrados.
	 */
	public Page<RespondeTransacaoDTO> listarTodasTransacoes(Pageable paginacao){
		return repository.findAll(paginacao).map(TransacaoMapper::toDTO);
	}	
}
