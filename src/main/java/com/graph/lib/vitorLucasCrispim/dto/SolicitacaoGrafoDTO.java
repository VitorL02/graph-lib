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
    @NotNull(message = "Distancia primeiro vertice não pode ser nulo")
    @Min(value = 0,message = "O valor do primeiro vertice de distancia deve ser igual ou maior que zero")
    private Integer primeiroVerticeDistancia;
    @NotNull(message = "Distancia segundo vertice não pode ser nulo")
    @Min(value = 0,message = "O valor do segundo vertice de distancia deve ser igual ou maior que zero")
    private Integer segundoVerticeDistancia;

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

    public Integer getPrimeiroVerticeDistancia() {
        return primeiroVerticeDistancia;
    }

    public void setPrimeiroVerticeDistancia(Integer primeiroVerticeDistancia) {
        this.primeiroVerticeDistancia = primeiroVerticeDistancia;
    }

    public Integer getSegundoVerticeDistancia() {
        return segundoVerticeDistancia;
    }

    public void setSegundoVerticeDistancia(Integer segundoVerticeDistancia) {
        this.segundoVerticeDistancia = segundoVerticeDistancia;
    }
}
