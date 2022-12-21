package com.alpha.system.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FilesStorageService {

	private String raiz = "/tmp/arquivos";
	private final Path root = Paths.get(raiz);
	

	  public void init() {
	    try {
	      Files.createDirectory(root);
	    } catch (IOException e) {
	      throw new RuntimeException("Pasta não inicializada para upload!");
	    }
	  }

	  public void save(MultipartFile file, String usuario) {
		  
		File destino = new File(raiz+"/"+usuario);
		if(!destino.exists()) {
			destino.mkdirs();
		}
	    try {
	    	Path roots = Paths.get(raiz+"/"+usuario+"/");
	    	log.info(roots.resolve(file.getOriginalFilename()).toString());
	    	Files.copy(file.getInputStream(), roots.resolve(file.getOriginalFilename()));
	    } catch (Exception e) {
	      throw new RuntimeException("Erro ao manipular o arquivo: " + e.getMessage());
	    }
	  }

	  public Resource load(String filename) {
	    try {
	      Path file = root.resolve(filename);
	      Resource resource = new UrlResource(file.toUri());

	      if (resource.exists() || resource.isReadable()) {
	        return resource;
	      } else {
	        throw new RuntimeException("Erro de leitura!");
	      }
	    } catch (MalformedURLException e) {
	      throw new RuntimeException("Erro: " + e.getMessage());
	    }
	  }

	  public void deleteAll() {
	    FileSystemUtils.deleteRecursively(root.toFile());
	  }

	  public Stream<Path> loadAll() {
	    try {
	      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
	    } catch (IOException e) {
	      throw new RuntimeException("Não foi possivel ler os arquivos!");
	    }
	  }
	
}
