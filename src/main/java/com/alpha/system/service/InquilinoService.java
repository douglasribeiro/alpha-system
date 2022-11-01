package com.alpha.system.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.alpha.entity.dto.InquilinoNewDto;
import com.alpha.entity.model.Endereco;
import com.alpha.entity.model.Inquilino;
import com.alpha.entity.model.Referencia;
import com.alpha.entity.model.Telefone;
import com.alpha.entity.repository.EnderecoRepository;
import com.alpha.entity.repository.InquilinoRepository;
import com.alpha.system.exception.ObjectNotFoundException;

@Service
public class InquilinoService {

	@Autowired
	InquilinoRepository inquilinoRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	public List<Inquilino> findAll(){
		return inquilinoRepository.findInquilinosAtivos(); 
	}
	
	public Inquilino findById(Long id) throws Exception {
		//return inquilinoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
		return inquilinoRepository.buscaPorId(id).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
	}

	public Inquilino save(Inquilino inquilino) {
		//Inquilino inquilino = inquilinoToDto(inquilinoNewDto);
		//inquilino.setId(null);
		Inquilino inq  = inquilinoRepository.save(inquilino);
		if(!inquilino.getEnderecos().isEmpty() || !inquilino.getTelefones().isEmpty() || !inquilino.getReferencias().isEmpty())
			update(inq.getId(), inquilino);
		return inq;
	}

	private Inquilino inquilinoToDto(InquilinoNewDto inquilinoNewDto) {
		Inquilino inq = new Inquilino(null
				, inquilinoNewDto.getNome()
				, inquilinoNewDto.getPessoa()
				, inquilinoNewDto.getCpfcnpj()
				, inquilinoNewDto.getIdentinscr()
				, inquilinoNewDto.getEmail()
				, inquilinoNewDto.getDtNiver()
				, inquilinoNewDto.getEstCivil()
				, inquilinoNewDto.getSexo()
				, inquilinoNewDto.getAtivo()
				, inquilinoNewDto.getNacional()
				, inquilinoNewDto.getNaturalidade()
				);
		return inq;
	}

	public Inquilino findByCpf(String cpf) {
		return inquilinoRepository.findByCpfcnpj(cpf).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
	}
	
	public Page<Inquilino> search(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		return inquilinoRepository.findPageInquilino(pageRequest);	
	}

	public void update(Long id, @Valid Inquilino inquilino) {
		Inquilino obj = inquilinoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
		if(inquilino.getEnderecos().size() < obj.getEnderecos().size()) {
			for(Endereco endereco: obj.getEnderecos()) {
				if(!inquilino.getEnderecos().contains(endereco)) {
					endereco.setInquilino(null);
				}
			}
		}
		obj = inquilino;
		
		List<Endereco> enderecos = inquilino.getEnderecos().stream().map(x -> 
		new Endereco(
				x.getId(), 
				x.getLogradouro(), 
				x.getNumero(), 
				x.getComplemento(), 
				x.getBairro(), 
				x.getCep(), 
				x.getTipoEndereco(), 
				inquilino, 
				x.getCidade()))
			.collect(Collectors.toList());
		Set<Telefone> telefones = new HashSet<>();
		for (Telefone telefone : inquilino.getTelefones()) {
			Telefone t = new Telefone(
					telefone.getId(), 
					inquilino, 
					telefone.getDdd(), 
					telefone.getNumero());
			telefones.add(t);
		}
		
		List<Referencia> referencias = inquilino.getReferencias().stream().map(r ->
			new Referencia(
					r.getId(), 
					r.getNome(), 
					r.getEmail(), 
					r.getPhone01(), 
					r.getPhone02(), 
					r.getObservacao(),
					inquilino)).collect(Collectors.toList());
	

		obj.setEnderecos(enderecos);
		obj.setTelefones(telefones);
		obj.setReferencias(referencias);
		obj.setId(id);
		inquilinoRepository.save(obj);
	}
	
}
