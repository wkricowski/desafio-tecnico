package com.tupi.desafio.validator.captura;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.mapper.TransacaoMapper;

public class ValidarDataValidadeCartaoTest {
	@Test
	@DisplayName("Deve dar exception se a TAG EMV '5F24' estiver ausente na transacao") 
	void testAusenciaDaTag5F24() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("5A0812345678901234569F3403040200")); // Tags 5A + 9F34
		ValidarDataValidadeCartao validacao = new ValidarDataValidadeCartao();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));

		Assertions.assertEquals("Tag EMV 5F24 (Data Validade) nao encontrada na lista recebida", exception.getMessage());
	}

	@Test
	@DisplayName("Deve dar exception se a data for inferior ao dia de hoje") 
	void testDataInferiorAHoje() {
		String dataOntem = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyMMdd"));

		var dto = new RecebeTransacaoDTO(String.format("5F2403%s",dataOntem));
		var transacao = TransacaoMapper.fromDTO(dto);
		ValidarDataValidadeCartao validacao = new ValidarDataValidadeCartao();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));
		
		Assertions.assertEquals("A data de validade não pode ser inferior à data atual.", exception.getMessage());
	}

	@Test
	@DisplayName("Se ano informado estiver entre 00 e 49, retorna 20YY. Se estiver entre 50 e 99, retorna 19YY") 
	void testRecuperacaoDeDatasMastercard() {
		ValidarDataValidadeCartao validacao = new ValidarDataValidadeCartao();

		// 1900/ ~
		Assertions.assertEquals(1950, validacao.recuperarLocalDate("500123").getYear()); // 1950/01/23
		Assertions.assertEquals(1999, validacao.recuperarLocalDate("990123").getYear()); // 1999/01/23

		// 2000/ ~
		Assertions.assertEquals(2000, validacao.recuperarLocalDate("000123").getYear()); // 2000/01/23
		Assertions.assertEquals(2049, validacao.recuperarLocalDate("490123").getYear()); // 2049/01/23
	}

	@Test
	@DisplayName("Deve dar exception ao informar uma data invalida") 
	void testDataInvalida() {
		ValidarDataValidadeCartao validacao = new ValidarDataValidadeCartao();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.recuperarLocalDate("2502")); //2025/02/??
		Assertions.assertEquals("A data de validade não corresponde ao formato yy/MM/dd.", exception.getMessage());
	}
}
