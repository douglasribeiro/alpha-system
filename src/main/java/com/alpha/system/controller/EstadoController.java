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
import com.alpha.entity.model.Estado;
import com.alpha.system.service.CargaEstadoCidadeService;

@RestController
@RequestMapping(value = "/estado")
public class EstadoController {
	
	@Autowired
	CargaEstadoCidadeService cargaEstadoCidadeService;
	
	@PostMapping()
	public ResponseEntity<List<Estado>> cargaEstado(@RequestBody List<Estado> estados) {
		return ResponseEntity.ok().body(cargaEstadoCidadeService.insertEstado(estados));
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Estado> getEstadoById(@PathVariable Integer id) throws Exception{
		return ResponseEntity.ok().body(cargaEstadoCidadeService.getEstadoById(id));
	}
	
	@GetMapping(value = "/sigla/{sigla}")
	public ResponseEntity<Estado> getEstadoBySigla(@PathVariable String sigla) throws Exception{
		return ResponseEntity.ok().body(cargaEstadoCidadeService.getEstadoBySigla(sigla));
	}
}