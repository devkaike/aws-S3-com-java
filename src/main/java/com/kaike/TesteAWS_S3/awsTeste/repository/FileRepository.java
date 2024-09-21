package com.kaike.TesteAWS_S3.awsTeste.repository;

import com.kaike.TesteAWS_S3.awsTeste.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFilename(String filename);
}