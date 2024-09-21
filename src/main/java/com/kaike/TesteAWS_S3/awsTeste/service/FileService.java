package com.kaike.TesteAWS_S3.awsTeste.service;

import com.kaike.TesteAWS_S3.awsTeste.domain.FileEntity;
import com.kaike.TesteAWS_S3.awsTeste.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public String uploadFile(String filename, String contentType, byte[] content) throws URISyntaxException {
        //String bucketName = "testekaike2003";
        //String region = "Europa (Estocolmo) eu-north-1";

        try {
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putOb, RequestBody.fromBytes(content));

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, filename);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public void saveFileDetails(String filename, String fileUrl, String contentType) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        fileEntity.setFileUrl(fileUrl);
        fileEntity.setContentType(contentType);
        fileRepository.save(fileEntity);
    }

    public Optional<FileEntity> getFileDetails(String filename) {
        return fileRepository.findByFilename(filename);
    }


}