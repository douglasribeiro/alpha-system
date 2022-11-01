package com.alpha.system;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.alpha.entity.model.Cidade;
import com.alpha.entity.model.Endereco;
import com.alpha.entity.model.Estado;
import com.alpha.entity.model.Inquilino;
import com.alpha.entity.model.Referencia;
import com.alpha.entity.model.Telefone;
import com.alpha.entity.model.Usuario;
import com.alpha.entity.model.enums.EstCivil;
import com.alpha.entity.model.enums.Perfil;
import com.alpha.entity.model.enums.Tipo;
import com.alpha.entity.model.enums.TipoEndereco;
import com.alpha.entity.repository.CidadeRepository;
import com.alpha.entity.repository.EnderecoRepository;
import com.alpha.entity.repository.EstadoRepository;
import com.alpha.entity.repository.InquilinoRepository;
import com.alpha.entity.repository.PessoaReposiotry;
import com.alpha.entity.repository.ReferenciaRepository;
import com.alpha.entity.repository.TelefoneRepository;
import com.alpha.entity.repository.UsuarioRepository;

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
			
			Inquilino inq1 = new Inquilino(1L, "Inquilino 01", Tipo.FISICO, "12345678901", "Identidade", "inquilino01@mail.com", null, EstCivil.SOLTEIRO, "Masculino", true, "Brasileiro", "São Paulo");
			Inquilino inq2 = new Inquilino(2L, "Inquilino 02", Tipo.FISICO, "12345678902", "Identidade", "inquilino02@mail.com", null, EstCivil.CASADO, "Masculino", true, "Brasileiro", "São Paulo");
			
			Estado est = new Estado(26, "São Paulo", "SP");
			
			Cidade sp = new Cidade();
			sp.setId(4741);
			sp.setEstado(est);
			
			Endereco end11 = new Endereco(1, "Alameda Paulista", "352", null, "Jardim Floridiana (Vila Xavier)", "14810256", TipoEndereco.RESIDENCIAL, inq1, sp);
			Endereco end12 = new Endereco(2, "Avenida Jorge Haddad	", "561", null, "Vila Cidade Industrial (Vila Xavier)", "14810244", TipoEndereco.COMERCIAL, inq1, sp);
			Endereco end21 = new Endereco(3, "Praça da Sé", "105", null, "Sé", "01001001", TipoEndereco.RESIDENCIAL, inq2, sp);
			
			Telefone tel1 = new Telefone(1, inq1, "16", "99711-1234");
			Telefone tel2 = new Telefone(2, inq1, "16", "3301-1111");
			Telefone tel3 = new Telefone(3, inq2, "16", "99725-1122");
			Telefone tel4 = new Telefone(4, inq2, "16", "3324-6840");
			
			Referencia ref01 = new Referencia(1L, "Referencia Primeiro", "ref.primeiro@mail.com", "(XX) XXXX-XXXX", "(YY) YYYY-YYYY", "Observação", inq1);
			
			inquilinoRepository.saveAll(Arrays.asList(inq1,inq2));
			enderecoRepository.saveAll(Arrays.asList(end11,end12,end21));
			telefoneRepository.saveAll(Arrays.asList(tel1,tel2,tel3,tel4));
			referenciaRepository.saveAll(Arrays.asList(ref01));
			
		}		
		
	}

}