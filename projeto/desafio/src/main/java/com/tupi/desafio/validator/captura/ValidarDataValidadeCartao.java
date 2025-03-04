package com.tupi.desafio.validator.captura;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesTagsMandatorias;

@Component
public class ValidarDataValidadeCartao implements ValidacoesTagsMandatorias{

	@Override
	public void validar(Transacao transacao) {
		String dataValidade = transacao.getTag("5F24").orElseThrow(() -> new ValidacaoException("Tag EMV Mandatória: 5F24 (Data Validade), não recebida."));

		// Valida se está no padrão esperado yyMMdd
		// https://emvlab.org/emvtags/?number=5f24
		recuperarLocalDate(dataValidade);
	}


	private LocalDate recuperarLocalDate(String data){
		try{
			int ano = Integer.parseInt(data.substring(0, 2));
			int mes = Integer.parseInt(data.substring(2, 4));
			int dia = Integer.parseInt(data.substring(4, 6));

			// Link: https://www.eftlab.com/knowledge-base/complete-list-of-emv-nfc-tags
			// 5F24 - Application Expiration Date - N6 (YYMMDD)
			// For MasterCard applications, if the value of YY ranges from '00' to '49' the date reads 20YYMMDD. If the value of YY ranges from '50' to '99' the date reads 19YYMMDD.
			int anoCompleto = (ano >= 50) ? 1900 + ano : 2000 + ano;
			return LocalDate.of(anoCompleto, mes, dia);
		}catch(Exception ex){
			throw new ValidacaoException("Tag EMV Mandatória: 5F24 (Data Validade) recebida, porém não corresponde ao formato yyMMdd.");
		}
	}
}
