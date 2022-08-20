package com.alpha.system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.entity.model.Usuario;
import com.alpha.system.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
	
	private UsuarioService usuarioService;
		
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> usuarioTeste() {
		return ResponseEntity.ok().body(usuarioService.findAll());
	}
	
	@GetMapping(value = "/teste")
	public ResponseEntity<List<Usuario>> usuario() {
		return ResponseEntity.ok().body(usuarioService.findAll());
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/xpto")
	public ResponseEntity<List<Usuario>> usuarioXpto() {
		return ResponseEntity.ok().body(usuarioService.findAll());
	}
	
	@PreAuthorize("hasAnyRole('ROLE_GERENTE', 'ROLE_ADMIN')")
	@GetMapping("{id}")
	public ResponseEntity<Usuario> usuarioId(@PathVariable("id") Integer id) {
		return ResponseEntity.ok().body(usuarioService.findById(id));
	}
}
