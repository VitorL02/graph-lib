package com.graph.lib.vitorLucasCrispim.entities;

public class Contadores {
    private Integer numeroVertices;
    private Integer contadorArestas;

    private Integer grauMaximo;

    private Integer grauMinimo;
    private Integer diametroGrafo;

    private Integer distanciaEntreVertices;

    private double mediaGraus;

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

    public Integer getDiametroGrafo() {
        return diametroGrafo;
    }

    public void setDiametroGrafo(Integer diametroGrafo) {
        this.diametroGrafo = diametroGrafo;
    }

    public Integer getDistanciaEntreVertices() {
        return distanciaEntreVertices;
    }

    public void setDistanciaEntreVertices(Integer distanciaEntreVertices) {
        this.distanciaEntreVertices = distanciaEntreVertices;
    }

    public double getMediaGraus() {
        return mediaGraus;
    }

    public void setMediaGraus(double mediaGraus) {
        this.mediaGraus = mediaGraus;
    }
}
