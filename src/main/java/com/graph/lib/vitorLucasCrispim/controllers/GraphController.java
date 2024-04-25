package com.graph.lib.vitorLucasCrispim.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graph.lib.vitorLucasCrispim.dto.SolicitacaoGrafoDTO;
import com.graph.lib.vitorLucasCrispim.entities.AuditoriaVO;
import com.graph.lib.vitorLucasCrispim.repositories.AuditoriaRepository;
import com.graph.lib.vitorLucasCrispim.services.GraphService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private static final String GRAFICO_UPLOAD_DESCRICAO = "Esse endpoint permite o upload de um Grafo  em formato .txt para processamento assincrono de um relatorio";

    private static final String GRAFICO_UPLOAD_RESUMO = "Esse endpoint permite o upload de um Grafo em formato .txt";

    private static final String GRAFICO_DOWNLOAD_DESCRICAO = "Esse endpoint realiza o download do arquivo relatorio em formato .zip que mostra informações como BFS,DFS diametro entre outros";

    private static final String GRAFICO_DOWNLOAD_RESUMO = "Esse endpoint permite o download de um arquivo .zip";

    private static final String GRAFICO_STATUS_DESCRICAO = "Esse endpoint retorna se o grafico foi gerado com sucesso, ou se ainda está em processamento";

    private static final String GRAFICO_STATUS_RESUMO = "Esse endpoint retorna o status de processamento do grafo";

    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public GraphController(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }
    @Autowired
    private GraphService graphService;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    @Operation(summary = GRAFICO_UPLOAD_RESUMO,description = GRAFICO_UPLOAD_DESCRICAO)
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
    @Operation(summary = GRAFICO_DOWNLOAD_RESUMO,description = GRAFICO_DOWNLOAD_DESCRICAO)
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
    @Operation(summary = GRAFICO_STATUS_RESUMO ,description = GRAFICO_STATUS_DESCRICAO)
    public ResponseEntity retornaStatusGeracaoArquivo(){
        List<AuditoriaVO> auditoria = auditoriaRepository.findAll();
        File resultDirectory = new File("result");
        if( (resultDirectory != null && resultDirectory.listFiles().length > 1)  && (auditoria.get(0) != null &&  auditoria.get(0).getMensagemProcessamento().equalsIgnoreCase("Relatorio Processado com sucesso!")) ){
            return ResponseEntity.status(HttpStatus.OK).body("Relatorio gerado com sucesso,favor realizar download");
        }
        if(resultDirectory != null && resultDirectory.listFiles().length <= 0){
            return ResponseEntity.status(HttpStatus.OK).body("Relatorio não solicitado, favor solicitar");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Relatorio ainda em processamento, favor aguardar");
    }




}
