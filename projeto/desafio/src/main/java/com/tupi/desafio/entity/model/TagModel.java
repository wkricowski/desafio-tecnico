package com.tupi.desafio.entity.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagModel(
	@Schema(description = "Identificaçao do conteúdo", example = "5A")
	String tag, 
	
	@Schema(description = "Tamanho do conteúdo (em bytes)", example = "8")
	Integer length, 
	
	@Schema(description = "Conteúdo em sí", example = "4111111111111111")
	String value) {

}
