package com.graph.lib.vitorLucasCrispim.dto;

import com.graph.lib.vitorLucasCrispim.enums.RepresentacaoGrafoEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class SolicitacaoGrafoDTO {
    @NotNull(message = "Vertice origem não pode ser nulo")
    @Min(value = 0,message = "O valor do vertice de origem deve ser igual ou maior que zero")
    private Integer verticeOrigem;

    @NotNull(message = "E necessario informar o tipo de grafo que sera gerado")
    private RepresentacaoGrafoEnum representacaoGrafo;

    public Integer getVerticeOrigem() {
        return verticeOrigem;
    }

    public void setVerticeOrigem(Integer verticeOrigem) {
        this.verticeOrigem = verticeOrigem;
    }

    public RepresentacaoGrafoEnum getRepresentacaoGrafo() {
        return representacaoGrafo;
    }

    public void setRepresentacaoGrafo(RepresentacaoGrafoEnum representacaoGrafo) {
        this.representacaoGrafo = representacaoGrafo;
    }
}
