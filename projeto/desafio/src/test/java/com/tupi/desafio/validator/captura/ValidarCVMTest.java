package com.tupi.desafio.validator.captura;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.mapper.TransacaoMapper;

public class ValidarCVMTest {

	@Test
	@DisplayName("Deve dar exception se a TAG EMV '9F34' estiver ausente na transacao") 
	void testAusenciaDaTag9F34() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("5A0812345678901234565F2403250520")); // Tags 5A + 5F24
		ValidarCVM validacao = new ValidarCVM();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));
		Assertions.assertEquals("Tag EMV 9F34 (CVM Results) não encontrada na lista recebida.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve dar exception o CVM nao tiver um tamanho válido") 
	void testTamanhoDaTag() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("9F34020402"));
		ValidarCVM validacao = new ValidarCVM();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));
		Assertions.assertEquals("Tag EMV 9F34 (CVM Results) é inválida.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve dar exception se o metodo do CVM não for suportado") 
	void testMetodosSuportado() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("9F34031E0200")); // 1E = Assinatura (Papel)
		ValidarCVM validacao = new ValidarCVM();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));
		Assertions.assertEquals("Tag EMV 9F34 (CVM Results) não possui um metodo suportado.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve dar exception a condição do CVM nao for suportada") 
	void testCondicaoCVM() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("9F3403020A02")); // xx0Axx = 0A = 10
		ValidarCVM validacao = new ValidarCVM();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));
		Assertions.assertEquals("Tag EMV 9F34 (CVM Results) não possui uma condição suportada.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve dar exception se o CVM estiver com o status de Falha") 
	void testCVMFalhou() {
		Transacao transacao = TransacaoMapper.fromDTO(new RecebeTransacaoDTO("9F3403020001")); // xxxx01 = Falha
		ValidarCVM validacao = new ValidarCVM();

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class,() -> validacao.validar(transacao));
		Assertions.assertEquals("Tag EMV 9F34 (CVM Results) não foi aprovado.", exception.getMessage());
	}
}
