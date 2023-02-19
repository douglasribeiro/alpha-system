package com.alpha.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.system.service.ImovelService;

@RestController
@RequestMapping(value = "/upload")
public class UploadController {
	
	@Autowired
	private ImovelService imovelService;
	
	@PostMapping
	public void requestUpdate(@RequestParam("file") MultipartFile file) {
		imovelService.upload(file, "15");
	}

}
