package com.graph.lib.vitorLucasCrispim.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_auditoria")
public class AuditoriaVO {

    private Long id;
    private String mensagemProcessamento;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "mensagem_processamento")
    public String getMensagemProcessamento() {
        return mensagemProcessamento;
    }

    public void setMensagemProcessamento(String mensagemProcessamento) {
        this.mensagemProcessamento = mensagemProcessamento;
    }
}
