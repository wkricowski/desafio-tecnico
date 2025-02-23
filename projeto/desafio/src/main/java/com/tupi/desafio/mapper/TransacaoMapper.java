package com.tupi.desafio.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tupi.desafio.dto.RecebeTransacaoDTO;
import com.tupi.desafio.entity.Transacao;
import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.exception.ValidacaoException;

/**
 * Classe responsável por transformar um DTO em Classe, durante o processo 
 * de transformação, é aplicada a lógica de decodificação/detalhamento das TLV
 */
public class TransacaoMapper {
	/**
	 * Transforma um RecebeTransacaoDTO em uma classe Transação
	 * 
	 * @param dados DTO contendo os dados EMV
	 * @return Classe transação com as tags EMV detalhadas
	 */
	public static Transacao fromDTO (RecebeTransacaoDTO dados){
		Transacao transacao = new Transacao();
		transacao.setConteudoEMV(dados.dadosEMV());
		transacao.setTags(decodificarTLV(dados.dadosEMV()));
		return transacao;
	}


	/**
	 * Metodo para decodificar um TLV de dados EMV
	 * 
	 * @param dados String contendo as TLVs
	 * @return Lista de TagModel
	 */
	private static List<TagModel> decodificarTLV(String dados) {
		List<TagModel> tlvList = new ArrayList<>();
		int posAtual = 0;

		try{
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
		}catch(Exception ex){
			throw new ValidacaoException("Lista de TAGs EMV informada é inválida.");
		}
	
        return tlvList;
    }

	/**
	 * Metodo para converter uma String hexadecimal em um array de bytes
	 * 
	 * @param dados String contendo o conteúdo em hexadecimal
	 * @return array de bytes
	 */
	private static byte[] hexStringToByteArray(String dados) {
		int length = dados.length();
		byte[] data = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			data[i / 2] = (byte) ((Character.digit(dados.charAt(i), 16) << 4) + Character.digit(dados.charAt(i + 1), 16));
		}
		return data;
	}
	
	/**
	 * Metodo para converter um array de bytes em String
	 * 
	 * @param dados array de bytes
	 * @return String com o conteudo
	 */
	private static String bytesToHex(byte[] dados) {
		StringBuilder sb = new StringBuilder();
		for (byte b : dados) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
}
