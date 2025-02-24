package com.tupi.desafio.validator.captura;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesTagsMandatorias;

@Component
public class ValidarPAN implements ValidacoesTagsMandatorias{

	@Override
	public void validar(Transacao transacao) {
		transacao.getTag("5A").orElseThrow(() -> new ValidacaoException("Tag EMV Mandatória: 5A (PAN), não recebida."));
	}
}
