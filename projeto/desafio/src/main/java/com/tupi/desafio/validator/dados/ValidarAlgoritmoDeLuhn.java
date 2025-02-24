package com.tupi.desafio.validator.dados;

import org.springframework.stereotype.Component;

import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.exception.ValidacaoException;
import com.tupi.desafio.interfaces.ValidacoesDosDados;

@Component
public class ValidarAlgoritmoDeLuhn implements ValidacoesDosDados{

	@Override
	public void validar(Transacao transacao) {
		var pan = transacao.getTag("5A").orElseThrow(() -> new IllegalStateException("Validação relacionada ao PAN, mas PAN não está presente."));

		if (!isLuhnValido(pan))
			throw new ValidacaoException("PAN inválido. Não passou no algoritmo de Luhn.");	
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
