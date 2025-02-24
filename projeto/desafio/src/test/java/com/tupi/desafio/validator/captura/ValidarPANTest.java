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
import com.tupi.desafio.exception.ValidacaoException;

public class ValidarPANTest {
	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarPAN validar;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAusenciaDaTag9F34(){
		// Arrange
		when(transacao.getTag("5A")).thenReturn(Optional.empty());

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("Tag EMV Mandatória: 5A (PAN), não recebida.", exception.getMessage());
	}
}
