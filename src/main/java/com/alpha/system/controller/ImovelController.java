package com.alpha.system.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alpha.entity.dto.ImovelDTO;
import com.alpha.entity.model.Imovel;
import com.alpha.entity.model.Inquilino;
import com.alpha.system.service.ImovelService;

@RestController
@RequestMapping(value = "/imovel")
public class ImovelController {

	@Autowired
	ImovelService imovelService;
	
	@GetMapping
	public ResponseEntity<List<Imovel>> findAll(){
		return ResponseEntity.ok().body(imovelService.findAll());
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Imovel> findById(@PathVariable("id") Long id) throws Exception{
		return ResponseEntity.ok().body(imovelService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid Imovel imovel) {
		Imovel obj = imovelService.save(imovel);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin("http://localhost:4200")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Imovel imovel, @PathVariable Long id) {
		imovelService.update(id, imovel);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		imovelService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
}
