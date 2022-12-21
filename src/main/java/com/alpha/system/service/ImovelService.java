package com.alpha.system.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.entity.model.Imovel;
import com.alpha.entity.repository.ImovelRepository;
import com.alpha.system.exception.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImovelService {
	
	private final String pathArquivos;
	private final ImovelRepository imovelRepository;

	public ImovelService(String pathArquivos, ImovelRepository imovelRepository) {
		this.imovelRepository = imovelRepository;
		this.pathArquivos = pathArquivos;
	}
	
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
		response.setFotos(imovel.getFotos());	
		return response;
	}

	public void delete(Long id) {
		imovelRepository.delete(imovelRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Imovel não encontrado")));
	}

	public ResponseEntity<String> upload(MultipartFile file, String usuario) {
		log.info("Recebendo arquivo(s)");
		
		File destino = new File(pathArquivos+usuario);
		if(!destino.exists()) {
			destino.mkdirs();
		}
		
		
		var caminho = pathArquivos+usuario+"/" + UUID.randomUUID() + "." + extrairExtensao(file.getOriginalFilename());
		log.info("Novo nome do arquivo: " + caminho);
		try {
			Files.copy(file.getInputStream(), Path.of(caminho), StandardCopyOption.REPLACE_EXISTING);
			return new ResponseEntity<String>("{\"mensagem\": \"Arquivo carregado com sucesso!\"} ", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"mensagem\": \"Erro ao carregar arquivo!\"} ", HttpStatus.INTERNAL_SERVER_ERROR);		
		}
		 
	}

	private String extrairExtensao(String nomeArquivo) {
		int i = nomeArquivo.lastIndexOf(".");
		return nomeArquivo.substring(i + 1);
	}

}
