package com.alpha.system.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alpha.entity.model.Imovel;
import com.alpha.system.service.FilesStorageService;
import com.alpha.system.service.ImovelService;
import com.alpha.system.util.ResponseMessage;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(value = "/imovel")
@Slf4j
@CrossOrigin("*")
public class ImovelController {
	
	private final FilesStorageService storageService;
	private final String pathArquivos;
	private final ImovelService imovelService;
	
	public ImovelController(@Value("${alpha-path-arquivos}") String pathArquivos, 
			ImovelService imovelService,
			FilesStorageService storageService
			) {
		this.pathArquivos = pathArquivos;
		this.imovelService = imovelService;
		this.storageService = storageService;
	}

	//@Autowired
	//ImovelService imovelService;
	
	@GetMapping
	public ResponseEntity<List<Imovel>> findAll(){
		log.info("Solicitação de lista de imoveis.");
		return ResponseEntity.ok().body(imovelService.findAll());
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Imovel> findById(@PathVariable("id") Long id) throws Exception{
		log.info("Solicitação do imovel " + id);
		return ResponseEntity.ok().body(imovelService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid Imovel imovel) {
		Imovel obj = imovelService.save(imovel);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin("http://localhost:4200")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody Imovel imovel, @PathVariable Long id) {
		imovelService.update(id, imovel);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		imovelService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
//	@PostMapping("/upload")
//	public ResponseEntity<String> salvarArquivo(@RequestParam("file") MultipartFile file, @RequestParam String usuario) {
//		
//		return imovelService.upload(file, usuario);
//		
//	}
//	
	 @PostMapping("/uploads")
	  public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam String usuario) {
	    String message = "";
	    try {
	      List<String> fileNames = new ArrayList<>();

	      Arrays.asList(files).stream().forEach(file -> {
	        storageService.save(file, usuario);
	        fileNames.add(file.getOriginalFilename());
	      });

	      message = "Uploaded the files successfully: " + fileNames;
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Fail to upload files!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }

	
	
}
