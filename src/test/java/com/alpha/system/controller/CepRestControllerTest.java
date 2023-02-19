package com.alpha.system.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alpha.entity.model.Cep;
import com.alpha.system.service.CepService;

@SpringBootTest
class CepRestControllerTest {
	
	private Cep cep;

	@Mock
	private CepService cepService;
	
	@InjectMocks
	private CepRestController cepRestController;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		cep = new Cep();
		cep.setCep("01001-000");
		cep.setLogradouro("Praça da Sé");
		cep.setComplemento("lado ímpar");      
		cep.setBairro("Sé");      
		cep.setLocalidade("São Paulo");     
		cep.setUf("SP");      
		cep.setIbge("3550308");      
		cep.setGia("1004");      
		cep.setDdd("11");      
		cep.setSiafi("7107");      
	}

	@Test
	void testGetCepSuccess() {
		when(cepService.buscaEnderecoPorCep("01001-000")).thenReturn(cep);
		
		ResponseEntity<Cep> response = cepRestController.getCep("01001-000");

		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
	}

	@Test
	void testGetCepFail() {
		when(cepService.buscaEnderecoPorCep("01001-000")).thenReturn(cep);
		
		ResponseEntity<Cep> response = cepRestController.getCep("01001-00X");

		assertNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		
	}
}
