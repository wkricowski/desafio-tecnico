package com.tupi.desafio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RecebeTransacaoDTO(
	@NotBlank
	@Schema(
		description = "Dados EMV da transação",
		example = "5A0841111111111111115F24033002229F3403040200"
	)
	String dadosEMV
	) {

}
