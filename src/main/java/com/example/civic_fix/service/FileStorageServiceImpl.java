package com.example.civic_fix.service;

import java.nio.file.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    @Override
    public String saveFile(MultipartFile file) {
        try {
            Files.createDirectories(rootLocation);

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destination = rootLocation.resolve(filename);

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return destination.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Path loadFile(String filePath) {
        return Paths.get(filePath);
    }
}