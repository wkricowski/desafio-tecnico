package com.tupi.desafio.validator.captura;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.mapper.TransacaoMapper;

public class ValidarPANTest {

	@Test
	@DisplayName("Deve dar exception se a TAG EMV '5A' estiver ausente na transacao") 
	void testAusenciaDaTag5A() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("5F24032505209F3403040200")); // Tags 5F24 + 9F34
		ValidarPAN validacao = new ValidarPAN();

		ValidacaoException exception = Assertions.assertThrows(ValidacaoException.class,() -> validacao.validar(transacao));

		Assertions.assertEquals("Tag EMV 5A (PAN), não encontrada na lista recebida.", exception.getMessage());
	}

	@Test
	@DisplayName("O PAN deve passar no algoritmo de Luhn")
	void testLuhn() {
		ValidarPAN validacao = new ValidarPAN();
		Assertions.assertTrue(validacao.isLuhnValido("4111111111111111"));
		Assertions.assertFalse(validacao.isLuhnValido("4111111111111112"));
	}

	@Test
	@DisplayName("O PAN deve conter o tamanho >= 13 e <= 19")
	void testTamanhoDoPAN() {
		ValidarPAN validacao = new ValidarPAN();

		Assertions.assertTrue(validacao.isTamanhoValidoPAN("1234567890123"));  // 13 dígitos (Válido)
		Assertions.assertFalse(validacao.isTamanhoValidoPAN("123456789012"));  // 12 dígitos (Inválido)
		Assertions.assertFalse(validacao.isTamanhoValidoPAN("12345678901234567890"));  // 20 dígitos (Inválido)
		Assertions.assertTrue(validacao.isTamanhoValidoPAN("1234567890123456789")); // 19 dígitos (Válido)
	}

}
