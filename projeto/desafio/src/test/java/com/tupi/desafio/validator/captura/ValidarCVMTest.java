package com.tupi.desafio.validator.captura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.exception.ValidacaoException;


public class ValidarCVMTest {
	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarCVM validar;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void testAusenciaDaTag9F34(){
		// Arrange
		when(transacao.getTag("9F34")).thenReturn(Optional.empty());

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("Tag EMV Mandatória: 9F34 (CVM Results), não recebida.", exception.getMessage());
	}

	@Test
	void testTagComFormatoInvalido(){
		// Arrange
		TagModel tagModel = new TagModel("9F34", 1, "02"); // ela espera 3 bytes (6 caracteres) como value
		when(transacao.getTag("9F34")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("Tag EMV Mandatória: 9F34 (CVM Results) recebida, porém com tamanho inválido.", exception.getMessage());
	}	
}
