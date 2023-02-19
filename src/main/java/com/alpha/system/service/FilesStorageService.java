package com.alpha.system.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.entity.model.Foto;
import com.alpha.entity.model.Imovel;
import com.alpha.entity.repository.FotoRepository;
import com.alpha.entity.repository.ImovelRepository;


@Service
public class FilesStorageService {

	@Autowired
	  private FotoRepository fotoRepository;
	@Autowired
	private ImovelRepository imovelRepository;

	  public Foto store(MultipartFile file, Long imovel) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    Foto foto = new Foto(fileName, file.getContentType(), file.getBytes(), imovelRepository.findById(imovel).get());

	    return fotoRepository.save(foto);
	  }

	  public Foto getFile(String id) {
	    return fotoRepository.findById(id).get();
	  }
	  
	  public Stream<Foto> getAllFiles() {
	    return fotoRepository.findAll().stream();
	  }
 
}