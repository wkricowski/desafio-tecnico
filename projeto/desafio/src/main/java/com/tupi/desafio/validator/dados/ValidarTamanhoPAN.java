package com.tupi.desafio.validator.dados;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesDosDados;

@Component
public class ValidarTamanhoPAN implements ValidacoesDosDados {

	@Override
	public void validar(Transacao transacao) {
		var pan = transacao.getTag("5A").orElseThrow(() -> new IllegalStateException("Validação relacionada ao PAN, mas PAN não está presente."));
		
		if (pan.length() < 13 || pan.length() > 19)
			throw new ValidacaoException("PAN inválido. O PAN deve ter entre 13 e 19 dígitos.");		
	}

}
