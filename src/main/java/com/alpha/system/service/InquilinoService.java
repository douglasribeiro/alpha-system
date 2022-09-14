package com.alpha.system.service;

import java.util.List;
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

	public Inquilino save(InquilinoNewDto inquilinoNewDto) {
		Inquilino inquilino = inquilinoToDto(inquilinoNewDto);
		inquilino.setId(null);
		
		return inquilinoRepository.save(inquilino);
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
		inq.setTelefones(inquilinoNewDto.getTelefones());
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
		
		//update endereco set inquilino_id = 3 where inquilino_id = null
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
		obj.setEnderecos(enderecos);
		obj.setId(id);
		inquilinoRepository.save(obj);
	}
	
}
