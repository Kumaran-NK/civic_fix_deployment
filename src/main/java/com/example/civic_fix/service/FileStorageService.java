package com.example.civic_fix.service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;

public interface  FileStorageService {
    String saveFile(MultipartFile file);
    Path loadFile(String filePath);
}
