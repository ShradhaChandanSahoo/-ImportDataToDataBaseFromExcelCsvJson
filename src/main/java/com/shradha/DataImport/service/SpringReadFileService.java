package com.shradha.DataImport.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.shradha.DataImport.model.User;

public interface SpringReadFileService {

	List<User> findAll();

	boolean saveDataFromUploadFile(MultipartFile file);

}
