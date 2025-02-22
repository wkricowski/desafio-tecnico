package com.tupi.desafio.dto;

import jakarta.validation.constraints.NotBlank;

public record RecebeTransacaoDTO(
	@NotBlank
	String dadosEMV) {

}
