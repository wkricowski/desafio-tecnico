package com.tupi.desafio.validator.dados;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

public class ValidarAlgoritmoDeLuhnTest {

	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarAlgoritmoDeLuhn validarAlgoritmoDeLuhn;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testPanAusente() {
		// Arrange
		when(transacao.getTag("5A")).thenReturn(Optional.empty());

		// Act & Assert
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validarAlgoritmoDeLuhn.validar(transacao));
		assertEquals("Validação relacionada ao PAN, mas PAN não está presente.", exception.getMessage());
	}

	@Test
	void testValidarPanValido() {
		// Arrange
		TagModel tagModel = new TagModel("5A", 8, "4111111111111111");
		when(transacao.getTag("5A")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		assertDoesNotThrow(() -> validarAlgoritmoDeLuhn.validar(transacao));
	}

	@Test
	void testValidarPanInvalido() {
		// Arrange
		TagModel tagModel = new TagModel("5A", 8, "4111111111111112"); // PAN inválido
		when(transacao.getTag("5A")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validarAlgoritmoDeLuhn.validar(transacao));
		assertEquals("PAN inválido. Não passou no algoritmo de Luhn.", exception.getMessage());
	}
}
