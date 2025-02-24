package com.tupi.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tupi.desafio.entity.Transacao;

/**
 * Interface responsável por acessar/manipular dados da classe Transacao no banco de dados via JPA.
 */
public interface TransacaoRepository extends JpaRepository<Transacao, Integer>{

}
