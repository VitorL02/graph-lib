package com.graph.lib.vitorLucasCrispim.services;

import com.graph.lib.vitorLucasCrispim.dto.SolicitacaoGrafoDTO;
import com.graph.lib.vitorLucasCrispim.entities.*;
import com.graph.lib.vitorLucasCrispim.enums.RepresentacaoGrafoEnum;
import com.graph.lib.vitorLucasCrispim.infra.ExceptionGenerica;
import com.graph.lib.vitorLucasCrispim.repositories.SolicitacaoGrafoRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
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
       // if(allSolicitations == null || allSolicitations.size() > 0){
        //    throw new ExceptionGenerica("Já existe uma solicitação em processamento, favor aguardar");
       // }
        solicitacaoGrafoVO.setDataSolicitacao(LocalDate.now());
        solicitacaoGrafoRepository.save(solicitacaoGrafoVO);

    }

    public void readGraphFileAndGenerateGraphs (SolicitacaoGrafoVO solicitacaoGrafoVO){
        try{
            Contadores contadores = new Contadores();
            contadores.setContadorArestas(0);
            contadores.setNumeroVertices(0);
            contadores.setGrauMaximo(0);
            contadores.setGrauMinimo(0);
            contadores.setDiametroGrafo(0);
            contadores.setDistanciaEntreVertices(0);
            contadores.setMediaGraus(0);
            File  file = new File("temp/grafo.txt");
            File result = File.createTempFile("result", ".txt");
            Path targetLocation = resultFileStorageLocation.resolve("result.txt");
            Files.createDirectories(targetLocation.getParent());
            Files.move(result.toPath(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File resultFile = new File("result/result.txt");
            Integer verticeOrigem = solicitacaoGrafoVO.getVerticeOrigem();
            Integer primeiroVerticeDistancia = solicitacaoGrafoVO.getPrimeiroVerticeDistancia();
            Integer segundoVerticeDistancia = solicitacaoGrafoVO.getSegundoVerticeDistancia();

            if( file.exists() && resultFile.exists() ){

                geraAlgoritimoBFS(file, resultFile,verticeOrigem,contadores,primeiroVerticeDistancia,segundoVerticeDistancia);

                geraAlgoritimoDFS(file,resultFile,verticeOrigem);


                if(solicitacaoGrafoVO.getRepresentacaoGrafo().equals(RepresentacaoGrafoEnum.MATRIZ)){
                    geraMatrizAdjacenteCasoSolicitado(file, resultFile,contadores);
                }else if(solicitacaoGrafoVO.getRepresentacaoGrafo().equals(RepresentacaoGrafoEnum.LISTA)){
                    geraListaAdjacenteCasoSolicitado(file,resultFile,contadores);
                }


                geraRelatorioFinal(contadores, resultFile,primeiroVerticeDistancia,segundoVerticeDistancia);

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
            result.delete();

        }catch (Exception e){
            throw new ExceptionGenerica(new StringBuilder().append("Erro ao enviar arquivo para processamento! ").append(e).toString());
        }
    }

    private static void geraAlgoritimoDFS(File file, File resultFile, Integer verticeOrigem) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(file));
        BufferedWriter escritor = new BufferedWriter(new FileWriter(resultFile,true));

        String linha;
        int V = Integer.parseInt(leitor.readLine());

        GraphDFS graphDFS = new GraphDFS();
        escritor.newLine();
        escritor.write("Grafo de Profundidade (DFS) :");
        escritor.newLine();
        escritor.flush();
        while((linha = leitor.readLine()) != null) {
            int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
            int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
            graphDFS.addEdge(primeiroVertice,segundoVertice);
        }

        graphDFS.DFSWriter(verticeOrigem,escritor);
        graphDFS.escreverComponentes(escritor);


    }

    private static void geraRelatorioFinal(Contadores contadores, File resultListAdjacente,
                                           Integer primeiroVerticeDistancia, Integer segundoVerticeDistancia) throws IOException {
        BufferedWriter escritor = new BufferedWriter(new FileWriter(resultListAdjacente,true));
        escritor.newLine();
        escritor.write("Relatorio: ");
        escritor.newLine();
        escritor.write(new StringBuilder().append("Numero total de Vertices: ").append(contadores.getNumeroVertices()).toString());
        escritor.newLine();
        escritor.write(new StringBuilder().append("Numero total de Arestas: ").append(contadores.getContadorArestas()).toString());
        escritor.newLine();
        escritor.write(new StringBuilder().append("Grau Minimo: ").append(contadores.getGrauMinimo()).toString());
        escritor.newLine();
        escritor.write(new StringBuilder().append("Grau Maximo: ").append(contadores.getGrauMaximo()).toString());
        escritor.newLine();
        escritor.write(new StringBuilder().append("Media Graus: ").append(contadores.getMediaGraus()).toString());
        escritor.newLine();
        escritor.write(new StringBuilder().append("Diametro do Grafo: ").append(contadores.getDiametroGrafo()).toString());
        escritor.newLine();
        escritor.write(new StringBuilder().append("Distancia entre os vertices ").append(primeiroVerticeDistancia).append(" ")
                .append(segundoVerticeDistancia).append(" é de: ").append(contadores.getDistanciaEntreVertices()).toString());
        escritor.flush();
        escritor.close();
    }


    private static void geraAlgoritimoBFS(File file, File resultListAdjacente, Integer verticeOrigem, Contadores contadores, Integer primeiroVerticeDistancia, Integer segundoVerticeDistancia) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(file));
        BufferedWriter escritor = new BufferedWriter(new FileWriter(resultListAdjacente,true));

        String linha;
        int V = Integer.parseInt(leitor.readLine());
        contadores.setNumeroVertices(V);


        GraphBFS graphBFS = new GraphBFS();

        escritor.write("Grafo de Largura (BFS) :");
        escritor.newLine();
        escritor.flush();
        while((linha = leitor.readLine()) != null) {
            int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
            int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
            graphBFS.addEdge(primeiroVertice,segundoVertice);
        }
        contadores.setDiametroGrafo(graphBFS.calcularDiametro());
        contadores.setDistanciaEntreVertices(graphBFS.distanciaEntreVertices(primeiroVerticeDistancia,segundoVerticeDistancia));
        graphBFS.BFSWriter(verticeOrigem,escritor);

    }

    private static void geraMatrizAdjacenteCasoSolicitado(File file, File resultListAdjacente, Contadores contadores) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(file));
        BufferedWriter escritor = new BufferedWriter(new FileWriter(resultListAdjacente,true));

        String linha;
        int V = Integer.parseInt(leitor.readLine());
        GraphMatrix graphMatrix = new GraphMatrix(V);

        while((linha = leitor.readLine()) != null) {
            int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
            int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
            graphMatrix.addEdge(primeiroVertice,segundoVertice);
            contadores.setContadorArestas(contadores.getContadorArestas() + 1);
        }
        graphMatrix.findMinAndMaxDegrees(contadores);
        escritor.write(graphMatrix.toString());
        escritor.flush();
        escritor.close();
    }


    private static void geraListaAdjacenteCasoSolicitado(File file, File result, Contadores contadores) throws IOException {
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(file));
            BufferedWriter escritor = new BufferedWriter(new FileWriter(result,true));
            String linha;
            int V = Integer.parseInt(leitor.readLine());
            int grauMinimo = Integer.MAX_VALUE;
            int grauMaximo = Integer.MIN_VALUE;
            int somaGraus = 0;
            int verticesWithEdges = 0;

            Map<Integer, List<Integer>> am = new HashMap<>();

            while((linha = leitor.readLine()) != null) {
                int primeiroVertice = Integer.parseInt(linha.split(" ")[0]);
                int segundoVertice = Integer.parseInt(linha.split(" ")[1]);
                addEdge(am, primeiroVertice, segundoVertice);
                contadores.setContadorArestas(contadores.getContadorArestas() + 1);
            }

            for (int vertex : am.keySet()) {
                int grau = am.get(vertex).size();
                if (grau > 0) {
                    verticesWithEdges++;
                    somaGraus += grau;
                    grauMinimo = Math.min(grauMinimo, grau);
                    grauMaximo = Math.max(grauMaximo, grau);
                }
            }

            contadores.setGrauMinimo(grauMinimo);
            contadores.setGrauMaximo(grauMaximo);
            contadores.setMediaGraus(verticesWithEdges > 0 ? (double) somaGraus / verticesWithEdges : 0);

            escritor.newLine();
            escritor.write("Lista Adjacente: ");
            escritor.newLine();
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
