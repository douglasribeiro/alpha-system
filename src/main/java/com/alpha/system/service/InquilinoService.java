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
import com.alpha.entity.model.Proprietario;
import com.alpha.entity.model.Referencia;
import com.alpha.entity.model.Telefone;
import com.alpha.entity.model.enums.TipoEndereco;
import com.alpha.entity.repository.EnderecoRepository;
import com.alpha.entity.repository.InquilinoRepository;
import com.alpha.entity.repository.ReferenciaRepository;
import com.alpha.entity.repository.TelefoneRepository;
import com.alpha.system.exception.ObjectNotFoundException;

@Service
public class InquilinoService {

	@Autowired
	InquilinoRepository inquilinoRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	@Autowired
	TelefoneRepository telefoneRepository;
	
	@Autowired
	ReferenciaRepository referenciaRepository;
	
	public List<Inquilino> findAll(){
		return inquilinoRepository.findInquilinosAtivos(); 
	}
	
	public Inquilino findById(Long id) throws Exception {
		//return inquilinoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
		return inquilinoRepository.buscaPorId(id).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
	}

	public Inquilino save(Inquilino inquilino) {
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
		return inq;
	}

	public Inquilino findByCpf(String cpf) {
		return inquilinoRepository.findByCpfcnpj(cpf).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
	}
	
	public Page<Inquilino> search(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		return inquilinoRepository.findPageInquilino(pageRequest);	
	}
	
	public void update(long id, @Valid Inquilino inquilino) {
		Inquilino obj = inquilinoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Inquilino não encontrado"));
		obj.setAtivo(inquilino.getAtivo());
		obj.setCpfcnpj(inquilino.getCpfcnpj());
		obj.setDtNiver(inquilino.getDtNiver());
		obj.setEmail(inquilino.getEmail());
		obj.setNacional(inquilino.getNacional());
		obj.setEstCivil(inquilino.getEstCivil());
		obj.setIdentinscr(inquilino.getIdentinscr());
		obj.setNaturalidade(inquilino.getNaturalidade());
		obj.setNome(inquilino.getNome());
		obj.setPessoa(inquilino.getPessoa());
		obj.setSexo(inquilino.getSexo());
		
		
		//Endereco(Inquilino inquilino, Proprietario proprietario) {
		
		List<Endereco> enderecos = inquilino.getEnderecos().stream().map(x -> 
		new Endereco(
				x.getId(), 
				x.getLogradouro(), 
				x.getNumero(), 
				x.getComplemento(), 
				x.getBairro(), 
				x.getCep(), 
				x.getTipoEndereco(),
				x.getCidade(),
				x.getEstado(),
				null,
				x.getProprietario()))
			.collect(Collectors.toList());
		
		List<Telefone> telefones = inquilino.getTelefones().stream().map(t -> 
			new Telefone(
					t.getId(),
					t.getDdd(),
					t.getNumero(),
					t.getTipo(),
					inquilino,
					t.getProprietario()
					)
		).collect(Collectors.toList());
		
		List<Referencia> referencias = inquilino.getReferencias().stream().map(r ->
			new Referencia(
					r.getId(), 
					r.getNome(), 
					r.getEmail(), 
					r.getPhone01(), 
					r.getPhone02(), 
					r.getObservacao(),
					inquilino,
					r.getProprietario())).collect(Collectors.toList());
		
		List<Endereco> removeEndereco = obj.getEnderecos()
				.stream()
				.filter(remov -> !inquilino.getEnderecos().contains(remov))
				.collect(Collectors.toList());
				
		
		List<Telefone> removeTelefone = obj.getTelefones()
				.stream()
				.filter(remov -> !inquilino.getTelefones().contains(remov))
				.collect(Collectors.toList());
		
		List<Referencia> removeReferencia = obj.getReferencias()
				.stream()
				.filter(remov -> !inquilino.getReferencias().contains(remov))
				.collect(Collectors.toList());

		obj.setEnderecos(enderecos);
		obj.setTelefones(telefones);
		obj.setReferencias(referencias);
		obj.setId(id);
		inquilinoRepository.save(obj);
		if(!removeEndereco.isEmpty()) {
			for(Endereco reg: removeEndereco) {
				enderecoRepository.deleteById(reg.getId());
			}
		}
		if(!removeTelefone.isEmpty()) {
			for(Telefone reg: removeTelefone) {
				telefoneRepository.deleteById(reg.getId());
			}
		}
		if(!removeReferencia.isEmpty()) {
			for(Referencia reg: removeReferencia) {
				referenciaRepository.deleteById(reg.getId());
			}
		}
	}

	
	public void delete(Long id) {
		inquilinoRepository.deleteById(id);
	}
	
}
