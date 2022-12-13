package com.alpha.system.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		List<Imovel> response = imovelRepository.findAll().stream().map(res -> writeTransiente(res)).collect(Collectors.toList());
		return response;
	}

	public Imovel findById(Long id) {
		return imovelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado"));
	}

	public Imovel save(Imovel imovel) {
		return imovelRepository.save(imovel);
	}
	
	private Imovel writeTransiente(Imovel imovel) {
		return new Imovel(
				imovel.getId(), 
				imovel.getEndereco(), 
				imovel.getEndereco().getLogradouro(), 
				imovel.getEndereco().getNumero(), 
				//imovel.getProprietario() != null ? imovel.getProprietario().getNome() : null,
				imovel.getMatricula(), 
				imovel.getComplemento(),  
				null, 
				imovel.getCondominio(), 
				imovel.getTipo().getCodigo(), 
				imovel.getEdificacao().getCodigo(), 
				imovel.getServico().getCodigo(), 
				imovel.getAreaTotal(), 
				imovel.getAreaConstruida(),
				imovel.getBanheiros(), 
				imovel.getQuartos(), 
				imovel.getSuites(), 
				imovel.getComodos(), 
				imovel.getVagas(), 
				imovel.getObservacao()); 
				//imovel.getProprietarios());
	}

}
