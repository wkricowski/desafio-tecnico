package com.tupi.desafio.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tupi.desafio.dto.ErroDTO;

@RestControllerAdvice
public class HttpResponse {

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity tratarErrosRegras(ValidacaoException ex) {
		return ResponseEntity.badRequest().body(new ErroDTO(ex.getMessage()));
	}
}
