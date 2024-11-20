package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageController {
    private final FileSystemStorageService fileSystemStorageService;

    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {

        Path file = fileSystemStorageService.retrieveImagePath(filename);
        byte[] data = Files.readAllBytes(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(MediaType.IMAGE_JPEG) // Adjust MIME type based on image type
                .body(data);
    }

}
