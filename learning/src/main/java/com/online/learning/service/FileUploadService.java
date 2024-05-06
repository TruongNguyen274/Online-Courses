package com.online.learning.service;

import com.online.learning.model.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    FileDTO uploadFile(MultipartFile multipartFile, String type);

    FileDTO uploadQuizExamFile(MultipartFile multipartFile);

    List<FileDTO> uploadMutilFile(MultipartFile[] multipartFiles, String type);

    FileDTO downloadFile(String filePath);

}
