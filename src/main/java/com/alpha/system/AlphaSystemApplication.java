package com.alpha.system;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.alpha.entity.model.Cidade;
import com.alpha.entity.model.Endereco;
import com.alpha.entity.model.Estado;
import com.alpha.entity.model.Inquilino;
import com.alpha.entity.model.Pessoa;
import com.alpha.entity.model.Usuario;
import com.alpha.entity.model.enums.EstCivil;
import com.alpha.entity.model.enums.Perfil;
import com.alpha.entity.model.enums.Tipo;
import com.alpha.entity.model.enums.TipoEndereco;
import com.alpha.entity.repository.CidadeRepository;
import com.alpha.entity.repository.EstadoRepository;
import com.alpha.entity.repository.InquilinoRepository;
import com.alpha.entity.repository.PessoaReposiotry;
import com.alpha.entity.repository.UsuarioRepository;

@SpringBootApplication
@ComponentScan({"com.alpha.system", "com.alpha.entity"})
@EntityScan({"com.alpha.system.domain", "com.alpha.entity.model"})
@EnableJpaRepositories({"com.alpha.system.repository", "com.alpha.entity.repository"})
public class AlphaSystemApplication {

	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PessoaReposiotry pessoaRepository;
	@Autowired
	private InquilinoRepository inquilinoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(AlphaSystemApplication.class, args);
	}
	
	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			
			Estado mg = new Estado(null, "Minas Gerais", "MG");
			Estado sp = new Estado(null, "São Paulo", "SP");
			Estado pr = new Estado(null, "Paraná", "PR");
			
			Cidade cid1 = new Cidade(null, "São Paulo", sp);
			Cidade cid2 = new Cidade(null, "Ribeirão Preto", sp);
			Cidade cid3 = new Cidade(null, "Belo Horizonte", mg);
			Cidade cid4 = new Cidade(null, "Curitiba", pr);
			
			if(!verEstado(mg)) estadoRepository.save(mg);
			if(!verEstado(sp)) estadoRepository.save(sp);
			if(!verEstado(pr)) estadoRepository.save(pr);
			
			if(!verCidade(cid1)) cidadeRepository.save(cid1);
			if(!verCidade(cid2)) cidadeRepository.save(cid2);
			if(!verCidade(cid3)) cidadeRepository.save(cid3);
			if(!verCidade(cid4)) cidadeRepository.save(cid4);
			
			Usuario user1 = new Usuario(null, "gerente", "gerente@mail.com", encoder.encode("123"));
			Usuario user2 = new Usuario(null, "admin", "admin@mail.com", encoder.encode("123"));
			Usuario user3 = new Usuario(null, "user", "user@mail.com", encoder.encode("123"));
			Usuario user4 = new Usuario(null, "user", "avulso@mail.com", encoder.encode("123"));
			
			user1.addPerfil(Perfil.GERENTE);
			user2.addPerfil(Perfil.ADMIN);
			user3.addPerfil(Perfil.USER);
			user4.addPerfil(Perfil.AVULSO);
			
			if(!verExist(user1)) usuarioRepository.save(user1);
			if(!verExist(user2)) usuarioRepository.save(user2);
			if(!verExist(user3)) usuarioRepository.save(user3);
			if(!verExist(user4)) usuarioRepository.save(user4);
			
			Pessoa pessoa = new Pessoa();
			pessoa.setId(null);
			pessoa.setEmail("pessoa@mail.com");
			pessoa.setNome("Pessoa");
			pessoa.setSenha(encoder.encode("123"));
			
			if(!verExistP(pessoa)) pessoaRepository.save(pessoa);
			
			Inquilino inq1 = new Inquilino(null, "Inquilino", Tipo.FISICO, "11111111111", "Identidade", "inquilino@mail.com", null, EstCivil.SOLTEIRO, "Masculino", true, "Brasileiro", "São Paulo");
			Inquilino inq2 = new Inquilino(null, "Inquilino", Tipo.JURIDICO, "11111111112", "Identidade", "inquilino2@mail.com", null, EstCivil.AMAZIADO, "Masculino", true, "Brasileiro", "Salvador");
			
			Set<String> tel = new HashSet<>();
			tel.add("12345678");
			tel.add("12345679");
			inq1.setTelefones(tel);
			
			Set<String> tel2 = new HashSet<>();
			tel2.add("12345670");
			tel2.add("12345671");
			inq2.setTelefones(tel2);
			
			Endereco end1 = new Endereco(null, "Rua um", "100", null, "Centro", "14800000", TipoEndereco.RESIDENCIAL, inq1, cid1);
			inq1.setEnderecos(Arrays.asList(end1));
			
			if(!verExistI(inq1)) inquilinoRepository.save(inq1);
			if(!verExistI(inq2)) inquilinoRepository.save(inq2);
		}
		
		private boolean verExist(Usuario usuario) {
			return usuarioRepository.findByEmail(usuario.getEmail()).isPresent();
		}
		
		private boolean verExistP(Pessoa pessoa) {
			return pessoaRepository.findByEmail(pessoa.getEmail()).isPresent();
		}
		
		private boolean verExistI(Inquilino inquilino) {
			return inquilinoRepository.findByEmail(inquilino.getEmail()).isPresent();
		}
		
		private boolean verEstado(Estado estado) {
			return estadoRepository.findByNome(estado.getNome()).isPresent(); 
		}

		private boolean verCidade(Cidade cidade) {
			return cidadeRepository.findByNome(cidade.getNome()).isPresent(); 
		}
	}

}
