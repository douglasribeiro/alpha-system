package com.alpha.system.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alpha.entity.model.Proprietario;
import com.alpha.system.service.ProprietarioService;

@RestController
@RequestMapping(value = "/proprietario")
public class ProprietarioController {
	
	@Autowired
	ProprietarioService proprietarioService;

	@GetMapping
	public ResponseEntity<List<Proprietario>> findAll(){
		return ResponseEntity.ok().body(proprietarioService.findAll());
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Proprietario> findById(@PathVariable("id") Long id) throws Exception{
		return ResponseEntity.ok().body(proprietarioService.findById(id));
	}
	
	@GetMapping(value="/cpf/{cpf}")
	public ResponseEntity<Proprietario> findByCpf(@PathVariable("cpf") String cpf) throws Exception{
		return ResponseEntity.ok().body(proprietarioService.findByCpf(cpf));
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid Proprietario proprietario) {
		Proprietario obj = proprietarioService.save(proprietario);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(value = "/page")
	public ResponseEntity<Page<Proprietario>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Proprietario> list = proprietarioService.search(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}
	
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Proprietario proprietario, @PathVariable Long id) {
		proprietarioService.update(id, proprietario);
		return ResponseEntity.noContent().build();
	}
	
	
}
