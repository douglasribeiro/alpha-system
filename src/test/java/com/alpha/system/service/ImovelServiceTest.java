package com.alpha.system.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.alpha.entity.model.Imovel;
import com.alpha.entity.repository.ImovelRepository;
import com.alpha.system.exception.ObjectNotFoundException;

class ImovelServiceTest {
	
	@InjectMocks
	private ImovelService service;

	@Mock
	private ImovelRepository repository;
	
	private Optional<Imovel> opImovel;
	private Imovel imovel;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		imovel = new Imovel(1L, "Proprietario Para Testes","987","Complemento imovel",0,0,0,0,"1000","80","1","2","0","4","1"
				,"Observacao", null,"Logrdouro A","357","Complemento endereco","Centro","14.810.244","Cidade","Estado");
		opImovel = Optional.of(new Imovel(1L, "Proprietario Para Testes","987","Complemento imovel",0,0,0,0,"1000","80","1","2","0","4","1"
				,"Observacao", null,"Logrdouro A","357","Complemento endereco","Centro","14.810.244","Cidade","Estado"));
	}


	@Test
	void testFindAllSuccess() {
		when(service.findAll()).thenReturn(List.of(imovel));
		
		List<Imovel> response = service.findAll();
		
		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals(Imovel.class, response.get(0).getClass());
	}

	@Test
	void testFindByIdSuccess() {
		when(repository.findById(1L)).thenReturn(opImovel);
		
		Imovel response = service.findById(1L);
		
		assertNotNull(response);
		assertEquals(Imovel.class, response.getClass());
	}
	
	@Test
	void testFindByIdFail() {
		when(repository.findById(anyLong())).thenThrow(new ObjectNotFoundException("Imovel não encontrado"));
		
		try {
			repository.findById(1L);
		} catch (Exception e) {
			assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Imovel não encontrado", e.getMessage());
		}
	}

	@Test
	void testSave() {
		when(repository.save(imovel)).thenReturn(imovel);
		
		Imovel response = repository.save(imovel);
		
		assertNotNull(response);
		assertEquals(Imovel.class, response.getClass());
		assertEquals(1L, response.getId());
	}

	@Test
	void testUpdate() {
		
	}

	@Test
	void testDelete() {
		
	}

	@Test
	void testUpload() {
		
	}

}
