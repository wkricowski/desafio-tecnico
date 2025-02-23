package com.tupi.desafio.validator.captura;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.interfaces.ValidacoesTLV;

@Component
public class ValidarPAN implements ValidacoesTLV{

	@Override
	public void validar(Transacao transacao) {
		String pan = transacao.getTags().stream()
			.filter(tag -> tag.tag().equalsIgnoreCase("5A"))
			.map(TagModel::value)
			.findFirst()
			.orElse("0");
			

		if (!isTamanhoValidoPAN(pan))
			System.out.println(pan.length());

		if (!isLuhnValido(pan))
			System.out.println("luhn invalido");
	}

	private boolean isTamanhoValidoPAN(String pan){
		return (pan.length() >= 13 && pan.length() <= 19);
	}


	// Algoritmo de Luhn
	// Link: https://en.wikipedia.org/wiki/Luhn_algorithm
	// Link: https://simplycalc.com/luhn-validate.php
	private boolean isLuhnValido(String pan){
		int soma = 0;
		boolean multiplicar = true;

		// Regras do algoritmo de Luhn:
		// 1. Remova o dígito de verificação do número (ultimo digito)
		// 2. Movendo-se da direita para a esquerda, dobre cada segundo digito começando pelo 'novo' último (já que o verificador foi removido)
		//    Se ao dobrar um dígito o resultado for > 9, subtraia 9 dele
		// 3. Some todos os dígitos resultantes (incluindo os que não foram duplicados)
		// 4. O dígito verificador é calculado por ""(10 - (s mod 10)) mod 10"", onde s é a soma da etapa 3.
		for (int i = pan.length() - 2; i >= 0; i--) {
			int digito = Character.getNumericValue(pan.charAt(i));

			if (multiplicar) {
				digito *= 2;
				if (digito > 9)	digito -= 9;
			}

			soma += digito;
			multiplicar = !multiplicar; // inverte para o proximo
		}
		
		int digitoVerificadorCalculado = (10 - (soma % 10)) % 10;
		int digitoVerificadorRecebido = Character.getNumericValue(pan.charAt(pan.length() - 1));
		return digitoVerificadorCalculado == digitoVerificadorRecebido;
	}
}
