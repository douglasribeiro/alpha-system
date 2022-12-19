package com.alpha.system.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.alpha.entity.dto.ProprietarioNewDto;
import com.alpha.entity.model.Endereco;
import com.alpha.entity.model.Proprietario;
import com.alpha.entity.model.Referencia;
import com.alpha.entity.model.Telefone;
import com.alpha.entity.repository.EnderecoRepository;
import com.alpha.entity.repository.ProprietarioRepository;
import com.alpha.system.exception.ObjectNotFoundException;

@Service
public class ProprietarioService {

	@Autowired
	ProprietarioRepository proprietarioRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	public List<Proprietario> findAll(){
		return proprietarioRepository.findProprietariosAtivos(); 
	}
	
	public Proprietario findById(Long id) throws Exception {
		return proprietarioRepository.buscaPorId(id).orElseThrow(() -> new ObjectNotFoundException("Proprietario não encontrado"));
	}

	public Proprietario save(Proprietario proprietario) {
		return null;
	}

	private Proprietario proprietarioToDto(ProprietarioNewDto proprietarioNewDto) {
		Proprietario inq = new Proprietario(null
				, proprietarioNewDto.getNome()
				, proprietarioNewDto.getPessoa()
				, proprietarioNewDto.getCpfcnpj()
				, proprietarioNewDto.getIdentinscr()
				, proprietarioNewDto.getEmail()
				, proprietarioNewDto.getDtNiver()
				, proprietarioNewDto.getEstCivil()
				, proprietarioNewDto.getSexo()
				, proprietarioNewDto.getAtivo()
				, proprietarioNewDto.getNacional()
				, proprietarioNewDto.getNaturalidade()
				);
		return inq;
	}

	public Proprietario findByCpf(String cpf) {
		return proprietarioRepository.findByCpfcnpj(cpf).orElseThrow(() -> new ObjectNotFoundException("Proprietario não encontrado"));
	}
	
	public Page<Proprietario> search(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		return proprietarioRepository.findPageProprietario(pageRequest);	
	}
	
	public void update(long id, @Valid Proprietario proprietario) {
		Proprietario obj = proprietarioRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Proprietario não encontrado"));
		obj.setAtivo(proprietario.getAtivo());
		obj.setCpfcnpj(proprietario.getCpfcnpj());
		obj.setDtNiver(proprietario.getDtNiver());
		obj.setEmail(proprietario.getEmail());
		obj.setNacional(proprietario.getNacional());
		obj.setEstCivil(proprietario.getEstCivil());
		obj.setIdentinscr(proprietario.getIdentinscr());
		obj.setNaturalidade(proprietario.getNaturalidade());
		obj.setNome(proprietario.getNome());
		obj.setPessoa(proprietario.getPessoa());
		obj.setSexo(proprietario.getSexo());
		
		
		List<Endereco> enderecos = proprietario.getEnderecos().stream().map(x -> 
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
				x.getInquilino(),
				proprietario))
			.collect(Collectors.toList());
		
		List<Telefone> telefones = proprietario.getTelefones().stream().map(t -> 
			new Telefone(
					t.getId(),
					t.getDdd(),
					t.getNumero(),
					t.getTipo(),
					t.getInquilino(),
					proprietario
					)
		).collect(Collectors.toList());
		
		List<Referencia> referencias = proprietario.getReferencias().stream().map(r ->
			new Referencia(
					r.getId(), 
					r.getNome(), 
					r.getEmail(), 
					r.getPhone01(), 
					r.getPhone02(), 
					r.getObservacao(),
					r.getInquilino(),
					proprietario)).collect(Collectors.toList());
	

		obj.setEnderecos(enderecos);
		obj.setTelefones(telefones);
		obj.setReferencias(referencias);
		obj.setId(id);
		proprietarioRepository.save(obj);
	}

	public Proprietario searchGeneric(String generic) {
		if(!proprietarioRepository.searchNome(generic).isEmpty())
			return proprietarioRepository.searchNome(generic).get();
		if(!proprietarioRepository.searchEmail(generic).isEmpty())
			return proprietarioRepository.searchEmail(generic).get();
		return null;
	}
	
}
