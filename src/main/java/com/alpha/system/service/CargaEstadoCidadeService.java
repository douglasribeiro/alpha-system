package com.alpha.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.entity.model.Cidade;
import com.alpha.entity.model.Estado;
import com.alpha.entity.repository.CidadeRepository;
import com.alpha.entity.repository.EstadoRepository;

@Service
public class CargaEstadoCidadeService {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Estado> insertEstado(List<Estado> estados) {
		return estadoRepository.saveAll(estados);
	}
	
	public List<Cidade> insertCidade(List<Cidade> cidades) {
		return cidadeRepository.saveAll(cidades);	 
	}

	public Estado getEstadoById(Integer id) {
		return estadoRepository.findById(id).orElse(new Estado());
	}

	public Estado getEstadoBySigla(String sigla) {
		return estadoRepository.findBySigla(sigla).orElse(new Estado());
	}

	public Cidade getCidadeById(Integer id) {
		return cidadeRepository.findById(id).orElse(new Cidade());
	}

	public List<Cidade> getCidadeByNome(String nome) {
		return cidadeRepository.findCidades(nome);
	}

	public List<Cidade> getCidadePorEstado(String sigla) {
		return cidadeRepository.findCidadesPorEstados(sigla);
	}
	
}
