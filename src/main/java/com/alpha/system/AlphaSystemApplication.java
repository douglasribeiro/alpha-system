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
			
		}
		
		private boolean verExist(Usuario usuario) {
			return usuarioRepository.findByEmail(usuario.getEmail()).isPresent();
		}
		
	}

}