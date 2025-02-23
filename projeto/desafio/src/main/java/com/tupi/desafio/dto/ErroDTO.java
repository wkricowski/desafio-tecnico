package com.tupi.desafio.dto;

import java.time.LocalDateTime;

public record ErroDTO(
	String erro, 
	LocalDateTime dtaHora
) {
	public ErroDTO(String erro){
		this(erro, LocalDateTime.now());
	}
}
