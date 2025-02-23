package com.tupi.desafio.service;

import java.util.List;

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

	public Transacao processarTransacao(RecebeTransacaoDTO dados){
		var transacao = TransacaoMapper.fromDTO(dados);
		
		// Percorre a lista de validações que implementam a 'interface/ValidacoesTLV.java'
		validadores.forEach(v->v.validar(transacao));

		return transacao;
	}
}
