package com.graph.lib.vitorLucasCrispim;

import com.graph.lib.vitorLucasCrispim.entities.SolicitacaoGrafoVO;
import com.graph.lib.vitorLucasCrispim.repositories.SolicitacaoGrafoRepository;
import com.graph.lib.vitorLucasCrispim.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class ScheduleJob {

    @Autowired
    private GraphService graphService;

    @Autowired
    private SolicitacaoGrafoRepository solicitacaoGrafoRepository;


    @Scheduled(fixedDelay = 30000)
    public void processaGrafo(){
        List<SolicitacaoGrafoVO> listaSolicitacoes = solicitacaoGrafoRepository.findAll();
        File file = new File("temp/grafo.txt");
        if( ( listaSolicitacoes != null && !listaSolicitacoes.isEmpty() ) && file.exists() ){
            System.out.println("*** Iniciando geração do grafo ***");
            graphService.readGraphFileAndGenerateGraphs(listaSolicitacoes.get(0));
            System.out.println("*** Finalizando geração do grafo ***");
        }
    }

}
