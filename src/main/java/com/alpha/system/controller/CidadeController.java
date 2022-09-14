package com.alpha.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.entity.model.Cidade;
import com.alpha.system.service.CargaEstadoCidadeService;

@RestController
@RequestMapping(value = "/cidade")
public class CidadeController {
	
	@Autowired
	CargaEstadoCidadeService cargaEstadoCidadeService;
	
	@PostMapping()
	public ResponseEntity<List<Cidade>> cargaCidade(@RequestBody List<Cidade> cidade) {		
		return ResponseEntity.ok().body(cargaEstadoCidadeService.insertCidade(cidade));
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Cidade> getCidadeById(@PathVariable Integer id) throws Exception{
		return ResponseEntity.ok().body(cargaEstadoCidadeService.getCidadeById(id));
	}
	
	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<List<Cidade>> getCidadeByNome(@PathVariable String nome) throws Exception{
		return ResponseEntity.ok().body(cargaEstadoCidadeService.getCidadeByNome(nome));
	}
	
	@GetMapping(value = "/{estado}")
	public ResponseEntity<List<Cidade>> getCidadesPorEstado(@PathVariable String estado) throws Exception{
		List<Cidade> obs = cargaEstadoCidadeService.getCidadePorEstado(estado);
		return ResponseEntity.ok().body(obs);
	}
}