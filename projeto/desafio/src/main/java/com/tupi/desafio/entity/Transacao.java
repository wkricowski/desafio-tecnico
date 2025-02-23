package com.tupi.desafio.entity;

import java.util.List;

import com.tupi.desafio.entity.model.TagModel;
import com.tupi.desafio.enums.StatusTransacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transacao {

	private StatusTransacao status;

	private String conteudoEMV;

	private Integer nsu;

	private List<TagModel> tags;

}
