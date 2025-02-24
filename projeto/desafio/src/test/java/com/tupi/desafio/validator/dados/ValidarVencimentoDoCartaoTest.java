package com.tupi.desafio.validator.dados;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.exception.ValidacaoException;

public class ValidarVencimentoDoCartaoTest {
	@Mock
	private Transacao transacao;

	@InjectMocks
	private ValidarVencimentoDoCartao validar;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testeVecimentoAusente() {
		// Arrange
		when(transacao.getTag("5F24")).thenReturn(Optional.empty());

		// Act & Assert
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validar.validar(transacao));
		assertEquals("Validação relacionada ao vencimento do cartão, mas vencimento não esta presente.", exception.getMessage());
	}

	@Test
	void testDataInferiorAHoje() {
		// Arrange
		String dataOntem = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyMMdd"));
		TagModel tagModel = new TagModel("5F24", dataOntem.length()/2, dataOntem);
		when(transacao.getTag("5F24")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validar.validar(transacao));
		assertEquals("A data de validade não pode ser inferior à data atual.", exception.getMessage());
	}

	@Test
	void testDataDeHojeDevePassar() {
		// Arrange
		String dataHoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		TagModel tagModel = new TagModel("5F24", dataHoje.length()/2, dataHoje);
		when(transacao.getTag("5F24")).thenReturn(Optional.of(tagModel.value()));

		// Act & Assert
		assertDoesNotThrow(() -> validar.validar(transacao));
	}
}
