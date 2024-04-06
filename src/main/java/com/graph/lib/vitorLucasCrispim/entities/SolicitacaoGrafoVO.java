package com.graph.lib.vitorLucasCrispim.entities;

import com.graph.lib.vitorLucasCrispim.enums.RepresentacaoGrafoEnum;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_solicitacao_grafo")
public class SolicitacaoGrafoVO {

    private Long id;
    private Integer verticeOrigem;
    private LocalDate dataSolicitacao;
    private RepresentacaoGrafoEnum representacaoGrafo;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "vertice_origem")
    public Integer getVerticeOrigem() {
        return verticeOrigem;
    }

    public void setVerticeOrigem(Integer verticeOrigem) {
        this.verticeOrigem = verticeOrigem;
    }
    @Column(name = "data_solicitacao")
    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
    @Column(name = "representacao_grafo")
    public RepresentacaoGrafoEnum getRepresentacaoGrafo() {
        return representacaoGrafo;
    }

    public void setRepresentacaoGrafo(RepresentacaoGrafoEnum representacaoGrafo) {
        this.representacaoGrafo = representacaoGrafo;
    }
}
