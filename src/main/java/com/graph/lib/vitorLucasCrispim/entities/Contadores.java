package com.graph.lib.vitorLucasCrispim.entities;

public class Contadores {
    private Integer numeroVertices;
    private Integer contadorArestas;

    private Integer grauMaximo;

    private Integer grauMinimo;

    public Integer getNumeroVertices() {
        return numeroVertices;
    }

    public void setNumeroVertices(Integer numeroVertices) {
        this.numeroVertices = numeroVertices;
    }

    public Integer getContadorArestas() {
        return contadorArestas;
    }

    public void setContadorArestas(Integer contadorArestas) {
        this.contadorArestas = contadorArestas;
    }

    public Integer getGrauMaximo() {
        return grauMaximo;
    }

    public void setGrauMaximo(Integer grauMaximo) {
        this.grauMaximo = grauMaximo;
    }

    public Integer getGrauMinimo() {
        return grauMinimo;
    }

    public void setGrauMinimo(Integer grauMinimo) {
        this.grauMinimo = grauMinimo;
    }
}
