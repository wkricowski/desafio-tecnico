package com.tupi.desafio.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.enums.StatusTransacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesDosDados;
import com.tupi.desafio.interfaces.ValidacoesTagsMandatorias;
import com.tupi.desafio.mapper.TransacaoMapper;
import com.tupi.desafio.repository.TransacaoRepository;
import com.tupi.desafio.validator.captura.ValidarCVM;
import com.tupi.desafio.validator.captura.ValidarPAN;
import com.tupi.desafio.validator.dados.ValidarAlgoritmoDeLuhn;
import com.tupi.desafio.validator.dados.ValidarTamanhoPAN;


@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

	// Exemplo de dados EMV de uma transacao válida para testar a CHAMADA de metodos
	private static final String TRANSACAO_VALIDA = "5A0841111111111111115F24032505209F3403040200";
	
	@InjectMocks
	private TransacaoService service;

	@Mock
	private TransacaoRepository repository;

	@Mock
	private AdministradoraService administradoraService;

	@Spy
	private List<ValidacoesTagsMandatorias> validadoresTags = new ArrayList<>();

	@Spy
	private List<ValidacoesDosDados> validadoresDados = new ArrayList<>();

	@Mock
	private ValidarPAN validacoesPAN;

	@Mock
	private ValidarCVM validacoesCVM;

	@Mock
	private ValidarTamanhoPAN validarTamanhoPan;

	@Mock
	private ValidarAlgoritmoDeLuhn validarAlgoritmoDeLuhn;

    @Captor
    private ArgumentCaptor<Transacao> transacaoCaptor;

	@Mock
	private TransacaoMapper transacaoMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Mock
	private Transacao transacao;

	@Test
	@DisplayName("Deve chamar a funcao 'save' se for uma transacao válida ")
	void testProcessarTransacao() {
		RecebeTransacaoDTO dto = new RecebeTransacaoDTO(TRANSACAO_VALIDA);

		service.processarTransacao(dto);

		BDDMockito.then(repository).should().save(any(Transacao.class));
	}

	@Test
	@DisplayName("Deve chamar a funcao 'validar' de todas as validacoes na lista de validadoresTags para confirmar o recebimento de tags mandatórias")
	void testValidadoresTagMandatorias() {
		// Arrange
		RecebeTransacaoDTO dto = new RecebeTransacaoDTO(TRANSACAO_VALIDA);

		BDDMockito.willDoNothing().given(validadoresDados).forEach(any());
		validadoresTags.add(validacoesPAN);
		validadoresTags.add(validacoesCVM);
		
		// Act
		service.processarTransacao(dto);

		// Assert
		// Verifica se o método 'validar' foi chamado uma vez para cada validador na lista
		BDDMockito.then(validacoesPAN).should(BDDMockito.times(1)).validar(any(Transacao.class));
		BDDMockito.then(validacoesCVM).should(BDDMockito.times(1)).validar(any(Transacao.class));
	}

	@Test
	@DisplayName("Deve chamar a funcao 'validar' de todas as validacoes na lista de validadoresDados para confirmar a estrutura correta/esperada dos dados")
	void testValidadoresDados() {
		// Arrange
		RecebeTransacaoDTO dto = new RecebeTransacaoDTO(TRANSACAO_VALIDA);

		BDDMockito.willDoNothing().given(validadoresTags).forEach(any());
		validadoresDados.add(validarTamanhoPan);
		validadoresDados.add(validarAlgoritmoDeLuhn);
		
		// Act
		service.processarTransacao(dto);

		// Assert
		// Verifica se o método 'validar' foi chamado uma vez para cada validador na lista
		BDDMockito.then(validarTamanhoPan).should(BDDMockito.times(1)).validar(any(Transacao.class));
		BDDMockito.then(validarAlgoritmoDeLuhn).should(BDDMockito.times(1)).validar(any(Transacao.class));
	}



	@Test
	@DisplayName("Deve lançar exceção se o TLV for invalido")
	void testFalhaNaValidacao() {
		RecebeTransacaoDTO dto = new RecebeTransacaoDTO("9F1A");

		ValidacaoException exception = Assertions.assertThrows( ValidacaoException.class, () -> service.processarTransacao(dto));

		Assertions.assertEquals("Lista de TAGs EMV informada é inválida.", exception.getMessage());
	}
	
	@Test
	@DisplayName("Deve chamar o metodo de envio para administradora")
	void testEnvioParaAdministradora() {
		RecebeTransacaoDTO dto = new RecebeTransacaoDTO(TRANSACAO_VALIDA);

		service.processarTransacao(dto);

		BDDMockito.then(administradoraService).should().enviarParaAdministradora(any(Transacao.class));
	}

	@Test
	@DisplayName("Deve ter status de recusada se nao for aprovada pela administradora")
	void testTransacaoRecusada() {
		RecebeTransacaoDTO dto = new RecebeTransacaoDTO(TRANSACAO_VALIDA);
		BDDMockito.given(administradoraService.enviarParaAdministradora(any(Transacao.class))).willReturn(StatusTransacao.RECUSADA);

		Transacao transacao = service.processarTransacao(dto);

		Assertions.assertEquals(StatusTransacao.RECUSADA, transacao.getStatus());
	}
}
