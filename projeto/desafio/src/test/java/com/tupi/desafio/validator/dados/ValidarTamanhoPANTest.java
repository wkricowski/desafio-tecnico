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

public class ValidarTamanhoPANTest {

	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarTamanhoPAN validar;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testPANAusente() {
		// Arrange
		when(transacao.getTag("5A")).thenReturn(Optional.empty());

		// Act & Assert
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validar.validar(transacao));
		assertEquals("Validação relacionada ao PAN, mas PAN não está presente.", exception.getMessage());
	}


	@Test
	void testValidarTamanhoMenorQue13() {
		// Arrange
		String pan = "123456789012";
		TagModel tagModel = new TagModel("5A", pan.length()/2, pan);
		when(transacao.getTag("5A")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("PAN inválido. O PAN deve ter entre 13 e 19 dígitos.", exception.getMessage());
	}

	@Test
	void testValidarTamanhoMinimo() {
		// Arrange
		String pan = "1234567890123"; // Atualmente, tamanho minimo aceito é 13
		TagModel tagModel = new TagModel("5A", pan.length()/2, pan);
		when(transacao.getTag("5A")).thenReturn(Optional.of(tagModel.value()));

		
		// Act & Assert
		assertDoesNotThrow(() -> validar.validar(transacao));
	}

	@Test
	void testValidarTamanhoMaximo() {
		String pan = "1234567890123456789"; // Atualmente, tamanho maximo aceito é 19
		// Arrange
		TagModel tagModel = new TagModel("5A", pan.length()/2, pan);
		when(transacao.getTag("5A")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		assertDoesNotThrow(() -> validar.validar(transacao));
	}

	@Test
	void testValidarTamanhoMaiorQue19() {
		// Arrange
		String pan = "12345678901234567890";
		TagModel tagModel = new TagModel("5A", pan.length()/2, pan);
		when(transacao.getTag("5A")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("PAN inválido. O PAN deve ter entre 13 e 19 dígitos.", exception.getMessage());
	}
}
