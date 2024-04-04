package com.graph.lib.vitorLucasCrispim.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphService {

    private final Path fileStorageLocation;

    private final Path resultFileStorageLocation;


    public GraphService(@Value("${file.upload.directory}")String fileUploadDirectory, @Value("${file.save.directory}")String resultUploadDirectory ) {
        this.fileStorageLocation = Paths.get(fileUploadDirectory);
        this.resultFileStorageLocation = Paths.get(resultUploadDirectory);
    }

    public void uploadGraphFile(MultipartFile file) throws Exception {
        var fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = "grafo.txt";
        Path targetLocation = fileStorageLocation.resolve(fileName);
        file.transferTo(targetLocation);
        readGraphFileAndGenerateGraphs();
    }

    public void readGraphFileAndGenerateGraphs (){
        try{
            File  file = new File("temp/grafo.txt");
            File result = File.createTempFile("resultListAdjacente", ".txt");
            Path targetLocation = resultFileStorageLocation.resolve("resultListAdjacente.txt");
            Files.createDirectories(targetLocation.getParent());
            Files.move(result.toPath(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File resultListAdjacente = new File("result/resultListAdjacente.txt");

            if(file.exists() && resultListAdjacente.exists() ){
                geraListaAdjacenteCasoSolicitado(file,resultListAdjacente);
            }

        }catch (Exception e){

        }
    }

    private static void geraListaAdjacenteCasoSolicitado(File file, File result) throws IOException {
        try  {
            BufferedReader leitor = new BufferedReader(new FileReader(file));
            BufferedWriter escritor = new BufferedWriter(new FileWriter(result));

            String linha;
            int V = Integer.parseInt(leitor.readLine());
            Map<Integer, List<Integer>> am = new HashMap<>();

            while((linha = leitor.readLine()) != null) {
                int testeUm = Integer.parseInt(linha.split(" ")[0]);
                int testDois = Integer.parseInt(linha.split(" ")[1]);
                addEdge(am, testeUm, testDois);
            }

            am.forEach((key, value) -> {
                try {
                    escritor.write("\nVertex " + key + ":");
                    System.out.print("\nVertex " + key + ":");
                    value.forEach(vertex -> {
                        System.out.print(" -> " + vertex);
                        try {
                            escritor.write(" -> " + vertex);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    escritor.newLine();
                    escritor.flush();
                    System.out.println();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void addEdge(Map<Integer, List<Integer>> am, int s, int d) {
        am.computeIfAbsent(s, k -> new ArrayList<>()).add(d);
        am.computeIfAbsent(d, k -> new ArrayList<>()).add(s);
    }




}
