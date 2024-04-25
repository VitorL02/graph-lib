package com.graph.lib.vitorLucasCrispim.repositories;

import com.graph.lib.vitorLucasCrispim.entities.SolicitacaoGrafoVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoGrafoRepository extends JpaRepository<SolicitacaoGrafoVO,Long> {
}
