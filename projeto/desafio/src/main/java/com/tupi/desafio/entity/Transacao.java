package com.tupi.desafio.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.enums.StatusTransacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "historico")
@Entity(name = "Transacao")
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Transacao {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_transacao")
	private StatusTransacao status;

	@Column(name = "dta_transacao", columnDefinition = "VARCHAR(20)")
	private String dtaTransacao;

	@Column(name = "conteudo_emv", columnDefinition = "TEXT")
	private String conteudoEMV;

	private Integer nsu;

	@Transient
	private List<TagModel> tags;

	public Transacao(){
		setDtaTransacao(LocalDateTime.now());
	}

	public void setDtaTransacao(LocalDateTime dt){
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
    	this.dtaTransacao = dt.format(f);
	}
}
