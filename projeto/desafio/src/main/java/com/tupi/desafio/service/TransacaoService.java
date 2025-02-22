package com.tupi.desafio.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.model.TagModel;

@Service
public class TransacaoService {

	public List<TagModel> processarTransacao(RecebeTransacaoDTO dados){
		var r = decodificarTLV(dados.dadosEMV());

		System.out.println(luhn("4111111111111111"));

		return r;
	}

	// Algoritmo de Luhn
	// Link: https://en.wikipedia.org/wiki/Luhn_algorithm
	private boolean luhn(String pan){
		int soma = 0;
		boolean multiplicar = true;

		// 1. Remova o dígito de verificação do número
		// 2. Movendo-se da direita para a esquerda
		for (int i = pan.length() - 2; i >= 0; i--) {		
			int digito = Character.getNumericValue(pan.charAt(i));

			// 2. dobre cada segundo dígito
			if (multiplicar) {
				digito = digito * 2;

			// 2. Se dobrar um dígito resultar em um valor > 9, subtraia 9 dele
			if (digito > 9)
				digito = digito - 9;
        }

			// 3. Some todos os dígitos resultantes (incluindo aqueles que não foram duplicados).
			soma += digito;
			
			// 2. dobre cada segundo dígito
			multiplicar = !multiplicar;
		}
		// 4. O dígito verificador é calculado por ""(10 - (s mod 10)) mod 10"", onde s é a soma da etapa 3.
		int digitoVerificadorCalculado = (10 - (soma % 10)) % 10;
		int digitoVerificadorRecebido = Character.getNumericValue(pan.charAt(pan.length() - 1));

		return digitoVerificadorCalculado == digitoVerificadorRecebido;
	}



	private List<TagModel> decodificarTLV(String dados){
		List<TagModel> tlvList = new ArrayList<>();
		int posAtual = 0;

		byte[] dados_bytes = hexStringToByteArray(dados);

		while (posAtual < dados_bytes.length) {            
			/* Verifica quantidade de bytes da TAG */
			int primeiroByte = dados_bytes[posAtual] & 0xFF;
			String tag;

			if ((primeiroByte & 0x1F) == 0x1F) {
				// TAG de 2 bytes
				tag = String.format("%02X%02X", dados_bytes[posAtual], dados_bytes[posAtual + 1]);
				posAtual += 2;
			} else { 
				// TAG de 1 byte
				tag = String.format("%02X", dados_bytes[posAtual]);
				posAtual += 1;
			}

			/* Verifica o Tamanho do Length */
			int tamanho;
			int primeiroByteDoLength = dados_bytes[posAtual] & 0xFF;

			if (primeiroByteDoLength <= 0x7F) { 
				// Length de 1 byte
				tamanho = primeiroByteDoLength;
				posAtual += 1;
			} else { 
				// Length de múltiplos bytes (0x81, 0x82, 0x83)
				int numBytes = primeiroByteDoLength & 0x7F;
				tamanho = 0;
				for (int i = 0; i < numBytes; i++) {
					tamanho = (tamanho << 8) | (dados_bytes[posAtual + 1 + i] & 0xFF);
				}
				posAtual += 1 + numBytes;
			}

			/* Recupera o Conteudo */
			String conteudo = bytesToHex(Arrays.copyOfRange(dados_bytes, posAtual, posAtual + tamanho));
			posAtual += tamanho;

			tlvList.add(new TagModel(tag, tamanho, conteudo));
		}

		return tlvList;
	}

	private byte[] hexStringToByteArray(String dados) {
		int length = dados.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte) ((Character.digit(dados.charAt(i), 16) << 4) + Character.digit(dados.charAt(i + 1), 16));
		}
		return data;
	}

	private String bytesToHex(byte[] dados) {
		StringBuilder sb = new StringBuilder();
		for (byte b : dados) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

}
