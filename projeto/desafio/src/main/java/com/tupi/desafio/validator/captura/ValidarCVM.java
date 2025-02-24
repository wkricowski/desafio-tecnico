package com.tupi.desafio.validator.captura;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesTagsMandatorias;

@Component
public class ValidarCVM  implements ValidacoesTagsMandatorias{

	@Override
	public void validar(Transacao transacao) {
		String resultadoCVM = transacao.getTag("9F34").orElseThrow(() -> new ValidacaoException("Tag EMV Mandatória: 9F34 (CVM Results), não recebida."));

		// Valida se está no tamanho esperado
		// https://emvlab.org/emvtags/?number=9F34
		if (resultadoCVM.length() != 6)
			throw new ValidacaoException("Tag EMV Mandatória: 9F34 (CVM Results) recebida, porém com tamanho inválido.");
	}
}
