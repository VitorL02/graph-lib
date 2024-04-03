package com.graph.lib.vitorLucasCrispim.controllers;

import com.graph.lib.vitorLucasCrispim.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private GraphService graphService;

    @PostMapping(path = "/upload",consumes = {"multipart/form-data"})
    public ResponseEntity upload(@RequestParam(value = "file") MultipartFile file) throws Exception {
        graphService.uploadGraphFile(file);
        return ResponseEntity.status(HttpStatus.OK).body("Processamento iniciado. Aguarde!");
    }
}
