package com.tupi.desafio.validator.dados;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesDosDados;

@Component
public class ValidarMetodoCVM implements ValidacoesDosDados {

	// Link: https://stackoverflow.com/questions/47000091/parse-cv-rule-from-cvm-list-for-emv
	// Link: https://paymentcardtools.com/emv-tag-decoders/cvm-results

	// 02 = Verificação de PIN criptografado online
	// 04 = Verificação de PIN criptografado pelo ICC
	// 1F = Nenhum CVM necessário
	private static final Set<String> METODOS_SUPORTADOS = Set.of("02", "04", "1F");

	@Override
	public void validar(Transacao transacao) {
		var cvm = transacao.getTag("9F34").orElseThrow(() -> new IllegalStateException("Validação relacionada ao CVM, mas CVM não está presente."));
		System.out.println(cvm);
		String metodo = cvm.substring(0, 2);

		if (!METODOS_SUPORTADOS.contains(metodo))
			throw new ValidacaoException("O CVM não possui um metodo suportado. (Suportados: 02, 04 ou 1F)");
	}

}
