package com.graph.lib.vitorLucasCrispim.services;

import com.graph.lib.vitorLucasCrispim.dto.SolicitacaoGrafoDTO;
import com.graph.lib.vitorLucasCrispim.entities.GraphBFS;
import com.graph.lib.vitorLucasCrispim.entities.GraphMatrix;
import com.graph.lib.vitorLucasCrispim.entities.SolicitacaoGrafoVO;
import com.graph.lib.vitorLucasCrispim.enums.RepresentacaoGrafoEnum;
import com.graph.lib.vitorLucasCrispim.infra.ExceptionGenerica;
import com.graph.lib.vitorLucasCrispim.repositories.SolicitacaoGrafoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GraphService {

    private final Path fileStorageLocation;

    private final Path resultFileStorageLocation;

    @Autowired
    private SolicitacaoGrafoRepository solicitacaoGrafoRepository;


    public GraphService(@Value("${file.upload.directory}")String fileUploadDirectory, @Value("${file.save.directory}")String resultUploadDirectory ) {
        this.fileStorageLocation = Paths.get(fileUploadDirectory);
        this.resultFileStorageLocation = Paths.get(resultUploadDirectory);
    }

    public void uploadGraphFile(MultipartFile file, SolicitacaoGrafoDTO solicitacaoGrafoDTO) throws Exception {
        var fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = "grafo.txt";
        Path targetLocation = fileStorageLocation.resolve(fileName);
        file.transferTo(targetLocation);
        SolicitacaoGrafoVO solicitacaoGrafoVO = new SolicitacaoGrafoVO();
        BeanUtils.copyProperties(solicitacaoGrafoDTO,solicitacaoGrafoVO);

       // List<SolicitacaoGrafoVO> allSolicitations = solicitacaoGrafoRepository.findAll();
       // if(allSolicitations == null || allSolicitations.size() >=0){
        //    throw new ExceptionGenerica("Já existe uma solicitação em processamento, favor aguardar");
       // }
        solicitacaoGrafoVO.setDataSolicitacao(LocalDate.now());
        solicitacaoGrafoRepository.save(solicitacaoGrafoVO);


    }

    public void readGraphFileAndGenerateGraphs (SolicitacaoGrafoVO solicitacaoGrafoVO){
        try{
            File  file = new File("temp/grafo.txt");
            File result = File.createTempFile("resultListAdjacente", ".txt");
            Path targetLocation = resultFileStorageLocation.resolve("resultListAdjacente.txt");
            Files.createDirectories(targetLocation.getParent());
            Files.move(result.toPath(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File resultListAdjacente = new File("result/resultListAdjacente.txt");
            Integer verticeOrigem = solicitacaoGrafoVO.getVerticeOrigem();

            if( file.exists() && resultListAdjacente.exists() ){

                geraAlgoritimoBFS(file, resultListAdjacente,verticeOrigem);

                if(solicitacaoGrafoVO.getRepresentacaoGrafo().equals(RepresentacaoGrafoEnum.MATRIZ)){
                    geraMatrizAdjacenteCasoSolicitado(file, resultListAdjacente);
                }else if(solicitacaoGrafoVO.getRepresentacaoGrafo().equals(RepresentacaoGrafoEnum.VETOR)){
                    geraListaAdjacenteCasoSolicitado(file,resultListAdjacente);
                }

                File resultPath = new File("result");
                File[] resultFiles = resultPath.listFiles();

                if(file.length() == 0){
                    throw new ExceptionGenerica("Não existem arquivos de resultado");
                }

                FileOutputStream fos = new FileOutputStream("result/zipFile.zip");
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                for(File zipThis : resultFiles){
                    FileInputStream fis = new FileInputStream(zipThis);
                    ZipEntry zipEntry = new ZipEntry(zipThis.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes =  new byte[2048];
                    int length;
                    while ((length = fis.read(bytes))  >= 0){
                        zipOut.write(bytes,0,length);
                    }
                    fis.close();
                }
                zipOut.close();
                fos.close();
            }
            solicitacaoGrafoRepository.deleteAll();

        }catch (Exception e){
            throw new ExceptionGenerica(new StringBuilder().append("Erro ao enviar arquivo para processamento! ").append(e).toString());
        }
    }

    private static void geraAlgoritimoBFS(File file, File resultListAdjacente, Integer verticeOrigem) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(file));
        BufferedWriter escritor = new BufferedWriter(new FileWriter(resultListAdjacente,true));

        String linha;
        int V = Integer.parseInt(leitor.readLine());
        GraphBFS graphBFS = new GraphBFS();

        while((linha = leitor.readLine()) != null) {
            int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
            int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
            graphBFS.addEdge(primeiroVertice,segundoVertice);
        }
        graphBFS.BFSWriter(verticeOrigem,escritor);
    }

    private static void geraMatrizAdjacenteCasoSolicitado(File file, File resultListAdjacente) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(file));
        BufferedWriter escritor = new BufferedWriter(new FileWriter(resultListAdjacente,true));

        String linha;
        int V = Integer.parseInt(leitor.readLine());
        GraphMatrix graphMatrix = new GraphMatrix(V);

        while((linha = leitor.readLine()) != null) {
            int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
            int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
            graphMatrix.addEdge(primeiroVertice,segundoVertice);

        }
        escritor.write(graphMatrix.toString());
        escritor.flush();
    }


    private static void geraListaAdjacenteCasoSolicitado(File file, File result) throws IOException {
        try  {
            BufferedReader leitor = new BufferedReader(new FileReader(file));
            BufferedWriter escritor = new BufferedWriter(new FileWriter(result,true));
            String linha;
            int V = Integer.parseInt(leitor.readLine());
            Map<Integer, List<Integer>> am = new HashMap<>();

            while((linha = leitor.readLine()) != null) {
                int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
                int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
                addEdge(am, primeiroVertice, segundoVertice);
            }

            am.forEach((key, value) -> {
                try {
                    escritor.write("\nVertex " + key + ":");
                    value.forEach(vertex -> {
                        try {
                            escritor.write(" -> " + vertex);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    escritor.newLine();
                    escritor.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            escritor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    static void addEdge(Map<Integer, List<Integer>> am, int s, int d) {
        am.computeIfAbsent(s, k -> new ArrayList<>()).add(d);
        am.computeIfAbsent(d, k -> new ArrayList<>()).add(s);
    }



}
