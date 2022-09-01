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
import com.alpha.entity.repository.UsuarioRepository;

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
	private PessoaReposiotry pessoaRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(AlphaSystemApplication.class, args);
	}
	
	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			
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
			
			Inquilino inq1 = new Inquilino(1L, "Inquilino 01", Tipo.FISICO, "12345678901", "Identidade", "inquilino01@mail.com", null, EstCivil.SOLTEIRO, "Masculino", true, "Brasileiro", "São Paulo");
			Inquilino inq2 = new Inquilino(2L, "Inquilino 02", Tipo.FISICO, "12345678902", "Identidade", "inquilino02@mail.com", null, EstCivil.CASADO, "Masculino", true, "Brasileiro", "São Paulo");
			
			Set<String> tel1 = new HashSet<>();
			tel1.add("123-4567");
			tel1.add("123-4568");
			inq1.setTelefones(tel1);
			Set<String> tel2 = new HashSet<>();
			tel2.add("123-7894");
			tel2.add("123-7895");
			inq2.setTelefones(tel2);
			
			Cidade sp = new Cidade();
			sp.setId(5270);
			
			Endereco end11 = new Endereco(1, "Rua Joaquim Penteado", "352", null, "Centro", "01123-456", TipoEndereco.RESIDENCIAL, inq1, sp);
			Endereco end12 = new Endereco(2, "Avenida Timbiras", "561", null, "Centro", "01123-843", TipoEndereco.COMERCIAL, inq1, sp);
			Endereco end21 = new Endereco(3, "Avenida Voluntarios da Patria", "105", null, "Parque São Rafael", "01122-159", TipoEndereco.RESIDENCIAL, inq2, sp);
			
			enderecoRepository.saveAll(Arrays.asList(end11,end12,end21));
			inquilinoRepository.saveAll(Arrays.asList(inq1,inq2));
			
		}
		
		private boolean verExist(Usuario usuario) {
			return usuarioRepository.findByEmail(usuario.getEmail()).isPresent();
		}
		
		
		
		
	}

}