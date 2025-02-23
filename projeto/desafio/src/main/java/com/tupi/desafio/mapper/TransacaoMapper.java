package com.tupi.desafio.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;

public class TransacaoMapper {

	public static Transacao fromDTO (RecebeTransacaoDTO dados){
		Transacao transacao = new Transacao();
		transacao.setEmv(dados.dadosEMV());
		transacao.setTags(decodificarTLV(dados.dadosEMV()));
		return transacao;
	}

	private static List<TagModel> decodificarTLV(String dados) {
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

	private static byte[] hexStringToByteArray(String dados) {
		int length = dados.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte) ((Character.digit(dados.charAt(i), 16) << 4) + Character.digit(dados.charAt(i + 1), 16));
		}
		return data;
	}
	
	private static String bytesToHex(byte[] dados) {
		StringBuilder sb = new StringBuilder();
		for (byte b : dados) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
}
