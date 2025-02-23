package com.tupi.desafio.validator.captura;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesTLV;

@Component
public class ValidarDataValidadeCartao implements ValidacoesTLV{

	@Override
	public void validar(Transacao transacao) {
		String dataValidade = transacao.getTags().stream()
				.filter(tag -> tag.tag().equalsIgnoreCase("5F24"))
				.map(TagModel::value)
				.findFirst()
				.orElseThrow(() -> new ValidacaoException("Tag EMV 5F24 (Data Validade) nao encontrada na lista recebida"));

		LocalDate dataCartao = recuperarLocalDate(dataValidade);

		if (dataCartao.isBefore(LocalDate.now()))
			throw new ValidacaoException("A data de validade não pode ser inferior à data atual.");
	}


	protected LocalDate recuperarLocalDate(String data){
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
			throw new ValidacaoException("A data de validade não corresponde ao formato yy/MM/dd.");
		}
	}
}
