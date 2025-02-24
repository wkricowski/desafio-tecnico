package com.tupi.desafio.validator.captura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.exception.ValidacaoException;

public class ValidarDataValidadeCartaoTest {

	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarDataValidadeCartao validar;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void testAusenciaDaTag5F24(){
		// Arrange
		when(transacao.getTag("5F24")).thenReturn(Optional.empty());

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("Tag EMV Mandatória: 5F24 (Data Validade), não recebida.", exception.getMessage());
	}


	@Test
	void testTagComFormatoInvalido(){
		// Arrange
		TagModel tagModel = new TagModel("5F24", 3, "252525");
		when(transacao.getTag("5F24")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("Tag EMV Mandatória: 5F24 (Data Validade) recebida, porém não corresponde ao formato yyMMdd.", exception.getMessage());
	}	
}
