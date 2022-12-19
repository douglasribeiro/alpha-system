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
		return writeTransiente(imovelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado")));
	}

	public Imovel save(Imovel imovel) {
		return imovelRepository.save(imovel);
	}
	
	public void update(Long id, Imovel imovel) {
		Imovel obj = imovelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado"));
		obj.setAreaConstruida(imovel.getAreaConstruida());
		obj.setAreaTotal(imovel.getAreaTotal());
		obj.setBanheiros(imovel.getBanheiros());
		obj.setComodos(imovel.getComodos());
		obj.setComplementoEndereco(imovel.getComplementoEndereco());
		obj.setComplementoImovel(imovel.getComplementoImovel());
		obj.setCondominio(imovel.getCondominio());
		obj.setEdificacao(imovel.getEdificacao());
		obj.setFotos(imovel.getFotos());
		obj.setLogradouro(imovel.getLogradouro());
		obj.setMatricula(imovel.getMatricula());
		obj.setNomProprietario(imovel.getNomProprietario());
		obj.setNumero(imovel.getNumero());
		obj.setObservacao(imovel.getObservacao());
		obj.setProprietario(imovel.getProprietario());
		obj.setQuartos(imovel.getQuartos());
		obj.setServico(imovel.getServico());
		obj.setSuites(imovel.getSuites());
		obj.setTipo(imovel.getTipo());
		obj.setVagas(imovel.getVagas());
		imovelRepository.save(obj);
	}
	
	
	
	private Imovel writeTransiente(Imovel imovel) {
		Imovel response = new Imovel(
				imovel.getId(),
				imovel.getNomProprietario(),
				imovel.getMatricula(),
				imovel.getComplementoImovel(),
				imovel.getCondominio().getCodigo(),
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
				imovel.getObservacao(),
				imovel.getProprietario(),
				imovel.getLogradouro(),
				imovel.getNumero(),
				imovel.getComplementoEndereco(),
				imovel.getBairro(),
				imovel.getCep(),
				imovel.getCidade(),
				imovel.getEstado());
		return response;
	}

	public void delete(Long id) {
		imovelRepository.delete(imovelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado")));
	}

}
