package com.tupi.desafio.dto;

import java.util.List;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.enums.StatusTransacao;

import io.swagger.v3.oas.annotations.media.Schema;

public record RespondeTransacaoDTO(
	@Schema(description = "ID da transação", example = "1")
	Integer id,

	@Schema(description = "Data e Hora da Transacao", example = "2025/02/23T12:02:58")
	String dtaTransacao,

	@Schema(description = "Status da transação", example = "APROVADA")
	StatusTransacao status,

	@Schema(description = "NSU (Número Sequencial Unico) da transação", example = "844122")
	Integer nsu,

	@Schema(description = "Conteudo EMV da transação (espelhado)", example = "5A0841111111111111115F24033002229F3403040200")
	String conteudoEMV,

	@Schema(description = "Lista com as TAGs EMV Decodificadas detalhadas com o Schema: TagModel")
	List<TagModel> tags
) {
	public RespondeTransacaoDTO (Transacao t){
		this(t.getId(), t.getDtaTransacao(), t.getStatus(), t.getNsu(), t.getConteudoEMV(), t.getTags());
	}		
}



