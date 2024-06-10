package com.graph.lib.vitorLucasCrispim;

import com.graph.lib.vitorLucasCrispim.entities.SolicitacaoGrafoVO;
import com.graph.lib.vitorLucasCrispim.infra.ExceptionGenerica;
import com.graph.lib.vitorLucasCrispim.repositories.AuditoriaRepository;
import com.graph.lib.vitorLucasCrispim.repositories.SolicitacaoGrafoRepository;
import com.graph.lib.vitorLucasCrispim.services.GraphService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleJob {

    @Autowired
    private GraphService graphService;

    @Autowired
    private SolicitacaoGrafoRepository solicitacaoGrafoRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;


    @Scheduled(fixedDelay = 10000)
    public void processaGrafo(){
        List<SolicitacaoGrafoVO> listaSolicitacoes = solicitacaoGrafoRepository.findAll();
        File file = new File("temp/grafo.txt");
        if( ( listaSolicitacoes != null && !listaSolicitacoes.isEmpty() ) && file.exists() ){
            System.out.println(new StringBuilder().append("*** Iniciando geração do grafo *** ").append(LocalDateTime.now()).toString());
            graphService.readGraphFileAndGenerateGraphs(listaSolicitacoes.get(0));
            System.out.println(new StringBuilder().append("*** Finalizando geração do grafo *** ").append(LocalDateTime.now()).toString());
        }
    }


   // @Scheduled(fixedDelay = 500000)
   // public void limpaDiretorioGrafo(){
     //   File resultDirectory = new File("result");
    //  try{
    //        if(resultDirectory != null && resultDirectory.listFiles().length > 1){
    //            System.out.println(new StringBuilder().append("*** Iniciando limpeza do diretorio de resultado *** ").append(LocalDateTime.now()).toString());
    //             FileUtils.cleanDirectory(resultDirectory);
    //           auditoriaRepository.deleteAll();
    //              System.out.println(new StringBuilder().append("*** Finalizando limpeza do diretorio de resultado *** "
    //            ).append(LocalDateTime.now()).toString());
    //         }
//      }catch (Exception e){
    //         throw new ExceptionGenerica(new StringBuilder().append("Erro ao limpar diretorio de resultados: ").append(e).toString());
    //       }

    //  }

}
