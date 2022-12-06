package com.alpha.system.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.alpha.entity.dto.ImovelDTO;
import com.alpha.entity.model.Imovel;
import com.alpha.entity.repository.ImovelRepository;
import com.alpha.system.exception.ObjectNotFoundException;

public class ImovelService {
	
	@Autowired
	ImovelRepository imovelRepository;

	public List<Imovel> findAll() {
		return imovelRepository.findAll();
	}

	public Imovel findById(Long id) {
		return imovelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado"));
	}

	public Imovel save(@Valid ImovelDTO imovelDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
