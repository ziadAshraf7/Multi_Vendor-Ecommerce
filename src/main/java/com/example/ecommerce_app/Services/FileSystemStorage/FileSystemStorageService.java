package com.example.ecommerce_app.Services.FileSystemStorage;

import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
@AllArgsConstructor
public class FileSystemStorageService {

    public final static String  defaultImageFilePath = "uploads/images";

    private final Path rootLocation = Paths.get(defaultImageFilePath);


    public String  saveImageToFileSystem(MultipartFile file) throws IOException {

        String fileName =  System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path targetLocation = rootLocation.resolve(fileName);

        Files.copy( file.getInputStream(), targetLocation);

        return fileName;
    }

    public Path retrieveImagePath(String imageFileName) throws IOException {
        return rootLocation.resolve(imageFileName).normalize();
    }

    public void deleteImageFile(String fileName) {
        try {
            Path filePath = rootLocation.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new CustomRuntimeException(e.getMessage());
        }
    }

    public boolean isImageFileExists(String fileName){
        Path path = rootLocation.resolve(fileName);
        return Files.exists(path);
    }
}
