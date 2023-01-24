package com.alpha.system.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
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
import com.alpha.system.util.FileUploadResponse;
import com.alpha.system.util.FileUtil;
import com.alpha.system.util.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/imovel")
@Slf4j
@CrossOrigin("*")
public class ImovelController {

	
	
	private final FilesStorageService storageService;
	private final ImovelService imovelService;

	public ImovelController(ImovelService imovelService,
			FilesStorageService storageService) {
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

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {
		String message = "";
		try {
			List<String> fileNames = new ArrayList<>();

			Arrays.asList(files).stream().forEach(file -> {
				storageService.save(file);
				fileNames.add(file.getOriginalFilename());
			});

			message = "Upload sucesso: " + fileNames;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Falha no upload!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	
	//----------------------------------------------------------------------------------------------------------------------
	
	/**
     * Method to upload multiple files
     * @param files
     * @return FileResponse
     */
    @PostMapping("/uploads")
    public ResponseEntity<FileUploadResponse> uploadFilesx(@RequestParam("files") MultipartFile[] files) {
        try {
        	Path filePathX = Paths.get("/");
            createDirIfNotExist();

            List<String> fileNames = new ArrayList<>();

            // read and write the file to the local folder
            Arrays.asList(files).stream().forEach(file -> {
                byte[] bytes = new byte[0];
                try {
                    bytes = file.getBytes();
                    Files.write(Paths.get(FileUtil.folderPath + file.getOriginalFilename()), bytes);
                    fileNames.add(file.getOriginalFilename());
                } catch (IOException e) {

                }
            });

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new FileUploadResponse("Files uploaded successfully: " + fileNames));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new FileUploadResponse("Exception to upload files!"));
        }
    }

    /**
     * Create directory to save files, if not exist
     */
    private void createDirIfNotExist() {
        //create directory to save the files
        File directory = new File(FileUtil.folderPath);
        if (! directory.exists()){
            directory.mkdir();
        }
    }

    /**
     * Method to get the list of files from the file storage folder.
     * @return file
     */
    @GetMapping("/files")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK)
                .body( new File(FileUtil.folderPath).list());
    }

}
