package com.alpha.system.controller;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
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

import com.alpha.entity.model.Foto;
import com.alpha.entity.model.Imovel;
import com.alpha.entity.model.ResponseMessage;
import com.alpha.system.message.ResponseFile;
import com.alpha.system.service.FilesStorageService;
import com.alpha.system.service.ImovelService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/imovel")
@Slf4j
@CrossOrigin("*")
public class ImovelController {

	private static final String C_IMOBILIARIA_FOTOS = "C:/imobiliaria/fotos/";
	private final FilesStorageService storageService;
	private final ImovelService imovelService;

	public ImovelController(ImovelService imovelService, FilesStorageService storageService) {
		this.imovelService = imovelService;
		this.storageService = storageService;
	}

	@GetMapping
	public ResponseEntity<List<Imovel>> findAll() {
		log.info("Solicitação de lista de imoveis.");
		return ResponseEntity.ok().body(imovelService.findAll());
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Imovel> findById(@PathVariable("id") Long id) throws Exception {
		log.info("Solicitação do imovel " + id);
		return ResponseEntity.ok().body(imovelService.findById(id));
	}

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody @Valid Imovel imovel) {
		Imovel obj = imovelService.save(imovel);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
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

	// ----------------------------------------------------------------------------------------------------------------------

//	@PostMapping("/uploads/{cliente}")
//	public ResponseEntity<ResponseMessage> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String cliente) {
//		String message = "";
//		String fileName = file.getOriginalFilename();
//		try {
//			String path = C_IMOBILIARIA_FOTOS + cliente + "/";
//			File dirPath = new File(path);
//			if (!dirPath.exists()) {
//				dirPath.mkdirs();
//			}
//			log.info("Upload imoveis sucesso.");
//			file.transferTo(new File(path + fileName));
//			message = "Upload imoveis sucesso: " + file.getOriginalFilename();
//		    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//		} catch (Exception e) {
//			message = "Não foi possivel fazer o upload do arquivo: " + file.getOriginalFilename() + "!";
//		    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//		}
//	}

//	@GetMapping("/uploads")
//	public ResponseEntity<List<FileInfo>> ListFiles() {
//		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
//			String filename = path.getFileName().toString();
//			String url = MvcUriComponentsBuilder
//					.fromMethodName(ImovelController.class, "getFile", path.getFileName().toString()).build()
//					.toString();
//
//			return new FileInfo(filename, url);
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	}

//	@GetMapping("/files/{filename:.+}")
//	public ResponseEntity<Resource> getFilex(@PathVariable String filename) {
//		Resource file = storageService.load(filename);
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}

//	@GetMapping("/uploads/{cliente}")
//	public ResponseEntity<List<String>> getListFiles(@PathVariable String cliente) throws IOException {
//		String paths = C_IMOBILIARIA_FOTOS + cliente;
//
//		String[] result;
//		File f = new File(paths);
//		result = f.list();
//		List<String> resultx = new ArrayList<>();
//		for (String unid : result) {
//			resultx.add(C_IMOBILIARIA_FOTOS + unid);
//		}
//		log.info("Listagem de arquivos.");
//		return ResponseEntity.status(HttpStatus.OK).body(resultx);
//	}

//	@GetMapping("/files")
//	  public ResponseEntity<List<FileInfo>> getListFiles() {
//	    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
//	      String filename = path.getFileName().toString();
//	      String url = MvcUriComponentsBuilder
//	          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
//
//	      return new FileInfo(filename, url);
//	    }).collect(Collectors.toList());
//
//	    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//	  }
	
	@PostMapping("/upload/{imovel}")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long imovel) {
	    String message = "";
	    try {
	      storageService.store(file,imovel);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }

	  @GetMapping("/files")
	  public ResponseEntity<List<ResponseFile>> getListFiles() {
	    List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
	      String fileDownloadUri = ServletUriComponentsBuilder
	          .fromCurrentContextPath()
	          .path("/files/")
	          .path(dbFile.getId())
	          .toUriString();

	      return new ResponseFile(
	          dbFile.getName(),
	          fileDownloadUri,
	          dbFile.getType(),
	          dbFile.getData().length);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(files);
	  }

	  @GetMapping("/files/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
	    Foto fileDB = storageService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	  }

}
