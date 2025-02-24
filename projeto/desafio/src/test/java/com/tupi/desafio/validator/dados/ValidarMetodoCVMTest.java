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

public class ValidarMetodoCVMTest {
	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarMetodoCVM validar;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCVMAusente() {
		// Arrange
		when(transacao.getTag("9F34")).thenReturn(Optional.empty());

		// Act & Assert
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validar.validar(transacao));
		assertEquals("Validação relacionada ao CVM, mas CVM não está presente.", exception.getMessage());
	}

	@Test
	void testMetodoNaoSuportado() {
		// Arrange
		String cvm = "990000";
		TagModel tagModel = new TagModel("9F34", cvm.length()/2, cvm);
		when(transacao.getTag("9F34")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("O CVM não possui um metodo suportado. (Suportados: 02, 04 ou 1F)", exception.getMessage());
	}

	@Test
	void testMetodoSuportado() {
		// Arrange
		String cvm = "020000";
		TagModel tagModel = new TagModel("9F34", cvm.length()/2, cvm);
		when(transacao.getTag("9F34")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		assertDoesNotThrow(() -> validar.validar(transacao));
	}
}
