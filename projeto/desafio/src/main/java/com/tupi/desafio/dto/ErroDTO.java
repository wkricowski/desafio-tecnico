package com.tupi.desafio.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErroDTO(
	@Schema(description = "Mensagem de erro", example = "Dados enviados são inválidos.")
	String erro, 

	@Schema(description = "Data e Hora da ocorrência", example = "2025/02/23T10:37:22")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd'T'HH:mm:ss")
	LocalDateTime dtaHora
) {
	public ErroDTO(String erro){
		this(erro, LocalDateTime.now());
	}
}
