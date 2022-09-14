package com.alpha.system.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.entity.model.Cep;
import com.alpha.entity.model.Cidade;
import com.alpha.entity.model.Estado;
import com.alpha.entity.repository.CidadeRepository;
import com.alpha.entity.repository.EstadoRepository;
import com.alpha.system.service.CepService;

@RestController
@RequestMapping(value = "/cep")
public class CepRestController {

    @Autowired
    private CepService cepService;
    
    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;

    @GetMapping("/{cepCompleto}")
    public ResponseEntity<Cep> getCep(@PathVariable String cepCompleto) {

        Cep cep = cepService.buscaEnderecoPorCep(cepCompleto);
        Optional<Cidade> cidade = cidadeRepository.findNomeAndUf(cep.getLocalidade(), cep.getUf());
        Optional<Estado> estado = estadoRepository.findBySigla(cep.getUf());
        cep.setIdCidade(cidade.get().getId());
        cep.setNomeUf(estado.get().getNome());
        cep.setIdUf(cidade.get().getEstado().getId());
        return cep != null ? ResponseEntity.ok().body(cep) : ResponseEntity.notFound().build(); 
    }

}