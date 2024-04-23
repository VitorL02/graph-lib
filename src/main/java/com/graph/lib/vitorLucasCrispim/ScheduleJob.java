package com.graph.lib.vitorLucasCrispim;

import com.graph.lib.vitorLucasCrispim.entities.SolicitacaoGrafoVO;
import com.graph.lib.vitorLucasCrispim.infra.ExceptionGenerica;
import com.graph.lib.vitorLucasCrispim.repositories.SolicitacaoGrafoRepository;
import com.graph.lib.vitorLucasCrispim.services.GraphService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
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


    @Scheduled(fixedDelay = 20000)
    public void processaGrafo(){
        List<SolicitacaoGrafoVO> listaSolicitacoes = solicitacaoGrafoRepository.findAll();
        File file = new File("temp/grafo.txt");
        if( ( listaSolicitacoes != null && !listaSolicitacoes.isEmpty() ) && file.exists() ){
            System.out.println("*** Iniciando geração do grafo ***");
            graphService.readGraphFileAndGenerateGraphs(listaSolicitacoes.get(0));
            System.out.println("*** Finalizando geração do grafo ***");
        }
    }


    @Scheduled(fixedDelay = 500000)
    public void limpaDiretorioGrafo(){
        File resultDirectory = new File("result");
        File fileDirectory = new File("temp");
        try{
            if(resultDirectory != null && resultDirectory.listFiles().length > 1){
                System.out.println("*** Iniciando limpeza do diretorio de resultado ***");
                FileUtils.cleanDirectory(resultDirectory);
                FileUtils.cleanDirectory(fileDirectory);
                System.out.println("*** Finalizando limpeza do diretorio de resultado ***");
            }
        }catch (Exception e){
            throw new ExceptionGenerica(new StringBuilder().append("Erro ao limpar diretorio de resultados: ").append(e).toString());
        }

    }

}
