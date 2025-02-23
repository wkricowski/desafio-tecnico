package com.tupi.desafio.validator.captura;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesTLV;

@Component
public class ValidarCVM  implements ValidacoesTLV{

	@Override
	public void validar(Transacao transacao) {
		String resultadoCVM = transacao.getTags().stream()
				.filter(tag -> tag.tag().equalsIgnoreCase("9F34"))
				.map(TagModel::value)
				.findFirst()
				.orElseThrow(() -> new ValidacaoException("Tag EMV 9F34 (CVM Results) não encontrada na lista recebida."));

		if (resultadoCVM.length() != 6)
			throw new ValidacaoException("Tag EMV 9F34 (CVM Results) é inválida.");

		String metodo = resultadoCVM.substring(0, 2);
		int condicao = Integer.parseInt(resultadoCVM.substring(2, 4), 16);
		String resultado = resultadoCVM.substring(4, 6);
		
		// Valida Byte 1 - CVM Performed
		if (!isMetodoSuportado(metodo))
			throw new ValidacaoException("Tag EMV 9F34 (CVM Results) não possui um metodo suportado.");
		
		// Valida Byte 2 - CVM Condition
		if (condicao > 9)
			throw new ValidacaoException("Tag EMV 9F34 (CVM Results) não possui uma condição suportada.");

		// Valida Byte 3 - CVM Result:  01=Failed
		if (resultado.equals("01"))
			throw new ValidacaoException("Tag EMV 9F34 (CVM Results) não foi aprovado.");
	}


	// Link: https://stackoverflow.com/questions/47000091/parse-cv-rule-from-cvm-list-for-emv
	// Link: https://paymentcardtools.com/emv-tag-decoders/cvm-results
	private Boolean isMetodoSuportado(String metodo){
		switch (metodo) {
			case "02":
				// Verificação de PIN criptografado online
				return true;
			case "04":
				// Verificação de PIN criptografado pelo ICC
				return true;
			case "1F":
				// Nenhum CVM necessário
				return true;
			case "42":
				// Verificação de PIN criptografado online
				return true;
			default:
				return false;				
		}
	}
}
