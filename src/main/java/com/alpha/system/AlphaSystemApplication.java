package com.alpha.system;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.alpha.entity.model.Endereco;
import com.alpha.entity.model.Imovel;
import com.alpha.entity.model.Inquilino;
import com.alpha.entity.model.Proprietario;
import com.alpha.entity.model.Referencia;
import com.alpha.entity.model.Telefone;
import com.alpha.entity.model.Usuario;
import com.alpha.entity.model.enums.EstCivil;
import com.alpha.entity.model.enums.Perfil;
import com.alpha.entity.model.enums.Tipo;
import com.alpha.entity.model.enums.TipoEndereco;
import com.alpha.entity.model.enums.TipoTelefone;
import com.alpha.entity.repository.EnderecoRepository;
import com.alpha.entity.repository.ImovelRepository;
import com.alpha.entity.repository.InquilinoRepository;
import com.alpha.entity.repository.ProprietarioRepository;
import com.alpha.entity.repository.ReferenciaRepository;
import com.alpha.entity.repository.TelefoneRepository;
import com.alpha.entity.repository.UsuarioRepository;
import com.alpha.system.controller.ImovelController;
import com.alpha.system.service.ImovelService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableFeignClients
@SpringBootApplication
@ComponentScan({"com.alpha.system", "com.alpha.entity"})
@EntityScan({"com.alpha.system.domain", "com.alpha.entity.model"})
@EnableJpaRepositories({"com.alpha.system.repository", "com.alpha.entity.repository"})
public class AlphaSystemApplication {


	@Autowired
	private InquilinoRepository inquilinoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private TelefoneRepository telefoneRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ReferenciaRepository referenciaRepository;
	@Autowired
	private ProprietarioRepository proprietarioRepository;
	@Autowired
	private ImovelRepository imovelRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public static void main(String[] args) {
		SpringApplication.run(AlphaSystemApplication.class, args);
	}
	
	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			
			Usuario user1 = new Usuario(1, "gerente", "gerente@mail.com", encoder.encode("123"));
			Usuario user2 = new Usuario(2, "admin", "admin@mail.com", encoder.encode("123"));
			Usuario user3 = new Usuario(3, "user", "user@mail.com", encoder.encode("123"));
			Usuario user4 = new Usuario(4, "user", "avulso@mail.com", encoder.encode("123"));
			
			user1.addPerfil(Perfil.GERENTE);
			user2.addPerfil(Perfil.ADMIN);
			user3.addPerfil(Perfil.USER);
			user4.addPerfil(Perfil.AVULSO);
			
			usuarioRepository.save(user1);
			usuarioRepository.save(user2);
			usuarioRepository.save(user3);
			usuarioRepository.save(user4);
				
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			Inquilino inq01 = new Inquilino(1l, "Inquilino 01", Tipo.FISICO, "cpfcnpj", "Identidade", "inq01@email.com", sdf.parse("01/10/1980 00:00"), EstCivil.SOLTEIRO, "Masculino", true, "Brasileiro", "São Paulo");
			inquilinoRepository.save(inq01);
			
			Endereco end04 = new Endereco(4, "Rua Portugal"    , "1433", null, "Jardim Europa"  , "14810075", TipoEndereco.ENTREGA, "Araraquara", "SP", inq01, null);
			Endereco end01 = new Endereco(1, "Rua Nove de Julho", "180", null, "Jardim Paulista", "14801295", TipoEndereco.RESIDENCIAL, "Araraquara", "SP", inq01, null);
			enderecoRepository.saveAll(Arrays.asList(end01,end04));
			
			Telefone telInq01 = new Telefone(1, "11", "3333-0001", TipoTelefone.PESSOAL, inq01, null);
			Telefone telInq02 = new Telefone(2, "11", "3333-0002", TipoTelefone.COMERCIAL, inq01, null);
			telefoneRepository.saveAll(Arrays.asList(telInq01,telInq02));
			
			Referencia refInq01 = new Referencia(1l, "Referencia 01", "referencia01@email.com", "(16)12345678", null, null, inq01, null);
			Referencia refInq02 = new Referencia(2l,"Inquilino 2", "email@email.com", "111", "222", "observ", inq01, null);
			referenciaRepository.saveAll(Arrays.asList(refInq01,refInq02));
			
			Proprietario prop01 = new Proprietario(1l, "Proprietario", Tipo.FISICO, "cpf", "Ident", "email@email.com", null, EstCivil.SOLTEIRO, "Masculino", true, "Brasileiro", "São Paulo");
			proprietarioRepository.save(prop01);
			
			Endereco end03 = new Endereco(3, "Rua Portugal", "1433", null, "Jardim Europa", "14801075", TipoEndereco.ENTREGA, "Araraquara", "SP", null, prop01);
			Endereco end02 = new Endereco(2, "Rua Nove de Julho", "180", null, "Jardim Paulista", "14801295", TipoEndereco.RESIDENCIAL, "Araraquara", "SP", null, prop01);
			enderecoRepository.saveAll(Arrays.asList(end02,end03));
			
			Telefone telInq03 = new Telefone(3, "11", "3333-0001", TipoTelefone.PESSOAL, null, prop01);
			Telefone telInq04 = new Telefone(4, "11", "3333-0002", TipoTelefone.COMERCIAL, null, prop01);
			telefoneRepository.saveAll(Arrays.asList(telInq03,telInq04));
			
			Referencia refInq03 = new Referencia(3l, "Referencia 01", "referencia01@email.com", "(16)12345678", null, null, null, prop01);
			Referencia refInq04 = new Referencia(4l,"Inquilino 2", "email@email.com", "111", "222", "observ", null, prop01);
			referenciaRepository.saveAll(Arrays.asList(refInq03,refInq04));
			
			Endereco endImov = new Endereco(null, "Rua Portugal", "800", null, "Jardim Europa", "14801075", TipoEndereco.ENTREGA, "Araraquara", "SP", null, null);
			Endereco endImov2 = new Endereco(2, "Rua Nove de Julho", "350", null, "Jardim Paulista", "14801295", TipoEndereco.RESIDENCIAL, "Araraquara", "SP", null, null);
			enderecoRepository.saveAll(Arrays.asList(endImov, endImov2));
			
			Imovel imovel = new Imovel(1L, "Propietario"  , "12345678" , "Complemento", 0, 0, 0, 0, "1200", "180", "2", "3", "1", "6", "1", "Obs", prop01, "Logradouro", "100", "Compl end", "Bairro", "14800-000", "Araraquara", "SP");
			Imovel imv02 = new Imovel(2L, "Proprietario 2", "123456788", "Compl"      , 0, 0, 0, 0, "3600", "120", "2", "3", "1", "6", "1", "Obs", prop01, "Logradouro", "100", "Compl end", "Bairro", "14800-000", "Araraquara", "SP");
			
			imovelRepository.saveAll(Arrays.asList(imovel,imv02));
			
			log.info("****************  Carga de Arquivos  ********************");
		}
		
	}

//	@Bean
//	public ImovelService imovelService() {
//		return new ImovelService(null, imovelRepository);
//	}
	
}