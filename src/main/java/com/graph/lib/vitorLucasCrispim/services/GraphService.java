package com.graph.lib.vitorLucasCrispim.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GraphService {

    private final Path fileStorageLocation;


    public GraphService(@Value("${file.upload.directory}")String fileUploadDirectory) {
        this.fileStorageLocation = Paths.get(fileUploadDirectory);
    }

    public void uploadGraphFile(MultipartFile file) throws Exception {
        var fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = "grafo.txt";
        Path targetLocation = fileStorageLocation.resolve(fileName);
        file.transferTo(targetLocation);
        readGraphFileAndGenerateGraphs();
    }

    public void readGraphFileAndGenerateGraphs (){
        File  file = new File("temp/grafo.txt");
        if(file.exists()){
            try{
                BufferedReader leitor = new BufferedReader(new FileReader(file));
                String linha = null;
                System.out.println("Quantidade de vértices:" + leitor.readLine());
                while((linha = leitor.readLine()) != null) {
                    System.out.println(linha.split(" ")[0]);
                }
            }catch (Exception e){

            }

        }


    }


}
