package com.alpha.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.entity.model.Cep;
import com.alpha.system.service.CepService;

@RestController
@RequestMapping(value = "/cep")
public class CepRestController {

    @Autowired
    private CepService cepService;
    
    @GetMapping("/{cepCompleto}")
    public ResponseEntity<Cep> getCep(@PathVariable String cepCompleto) {
        Cep cep = cepService.buscaEnderecoPorCep(cepCompleto);
        return cep != null ? ResponseEntity.ok().body(cep) : ResponseEntity.notFound().build(); 
    }

}