package com.alpha.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alpha.entity.model.Usuario;
import com.alpha.entity.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
	public List<Usuario> findAll(){
		return usuarioRepository.findAll();
	}

	public Usuario findById(Integer id) {
		Usuario user = usuarioRepository.findById(id).orElse(null);
		return user;
	}
	
}
