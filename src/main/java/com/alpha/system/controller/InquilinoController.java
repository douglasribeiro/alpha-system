package com.alpha.system.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alpha.entity.dto.InquilinoNewDto;
import com.alpha.entity.model.Inquilino;
import com.alpha.system.service.InquilinoService;

@RestController
@RequestMapping(value = "/inquilino")
public class InquilinoController {
	
	@Autowired
	InquilinoService inquilinoService;

	@GetMapping
	public ResponseEntity<List<Inquilino>> findAll(){
		return ResponseEntity.ok().body(inquilinoService.findAll());
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Inquilino> findById(@PathVariable("id") Long id) throws Exception{
		return ResponseEntity.ok().body(inquilinoService.findById(id));
	}
	
	@GetMapping(value="/cpf/{cpf}")
	public ResponseEntity<Inquilino> findByCpf(@PathVariable("cpf") String cpf) throws Exception{
		return ResponseEntity.ok().body(inquilinoService.findByCpf(cpf));
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid InquilinoNewDto inquilinoNewDto) {
		Inquilino obj = inquilinoService.save(inquilinoNewDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping(value = "/page")
	public ResponseEntity<Page<Inquilino>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Inquilino> list = inquilinoService.search(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Inquilino inquilino, @PathVariable Long id) {
		inquilinoService.update(id, inquilino);
		return ResponseEntity.noContent().build();
	}
	
	
}
