package com.graph.lib.vitorLucasCrispim.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graph.lib.vitorLucasCrispim.dto.SolicitacaoGrafoDTO;
import com.graph.lib.vitorLucasCrispim.services.GraphService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public GraphController(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }
    @Autowired
    private GraphService graphService;


    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity upload(@RequestPart(value = "file") MultipartFile file, @RequestPart(value ="solicitacaoGrafoDTO" ) String solicitacaoGrafoDTOJSON) throws Exception {

        SolicitacaoGrafoDTO solicitacaoGrafoDTO = new ObjectMapper().readValue(solicitacaoGrafoDTOJSON, SolicitacaoGrafoDTO.class);

        BindingResult bindingResult = new BeanPropertyBindingResult(solicitacaoGrafoDTO, "solicitacaoGrafoDTO");
        validator.validate(solicitacaoGrafoDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }


        graphService.uploadGraphFile(file,solicitacaoGrafoDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Processamento iniciado. Aguarde!");
    }

    @GetMapping(path = "/download")
    public ResponseEntity dowload (HttpServletRequest request, HttpServletResponse response) throws IOException{
        Path file = Paths.get("result/zipFile.zip");
        File resultZipFile = new File("temp/grafo.txt");
        String contentType = Files.probeContentType(file);

        if(resultZipFile == null || !resultZipFile.exists()){
            return ResponseEntity.internalServerError().body("O Grafo ainda encontra-se em processamento, favor aguardar");
        }

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        response.setContentType(contentType);
        response.setContentLengthLong(Files.size(file));
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                .filename(file.getFileName().toString(), StandardCharsets.UTF_8)
                .build()
                .toString());
        Files.copy(file, response.getOutputStream());

        return ResponseEntity.status(HttpStatus.OK).build();

    }
    @GetMapping(path = "status")
    public ResponseEntity retornaStatusGeracaoArquivo(){
        File resultDirectory = new File("result");
        if(resultDirectory != null && resultDirectory.listFiles().length > 1){
            return ResponseEntity.status(HttpStatus.OK).body("Relatorio gerado com sucesso,favor realizar download");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Relatorio ainda em processamento, favor aguardar");
    }




}
