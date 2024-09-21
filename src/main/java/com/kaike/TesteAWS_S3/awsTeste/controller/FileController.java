package com.kaike.TesteAWS_S3.awsTeste.controller;

import com.kaike.TesteAWS_S3.awsTeste.domain.FileEntity;
import com.kaike.TesteAWS_S3.awsTeste.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file provided");
        }

        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();

        try {
            byte[] content = file.getBytes();
            String fileUrl = fileService.uploadFile(filename, contentType, content);
            fileService.saveFileDetails(filename, fileUrl, contentType);

            return ResponseEntity.ok().body(fileUrl);
        } catch (IOException | URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<?> getFileDetails(@PathVariable String filename) {
        Optional<FileEntity> fileEntityOpt = fileService.getFileDetails(filename);

        if (fileEntityOpt.isPresent()) {
            FileEntity fileEntity = fileEntityOpt.get();
            return ResponseEntity.ok().contentType(MediaType.valueOf(fileEntity.getContentType()))
                    .body(fileEntity.getFileUrl());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }
}