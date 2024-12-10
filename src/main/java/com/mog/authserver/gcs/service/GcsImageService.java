package com.mog.authserver.gcs.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.mog.authserver.gcs.util.GcsURLParser;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GcsImageService {

    private final Storage storage;

    @Value("${gcp.bucket-name}")
    private String bucketName;

    // 파일 업로드 메서드
    public String uploadFile(MultipartFile file) {
        String fileName = generateFileName(Objects.requireNonNull(file.getOriginalFilename())); // 고유 파일 이름 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo, file.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return GcsURLParser.generateURL(bucketName, fileName); // 업로드된 파일 이름 반환
    }

    public void deleteFile(String imageUri) {
        String filename = GcsURLParser.parseFileName(imageUri);
        BlobId blobId = BlobId.of(bucketName, filename);
        if (!storage.delete(blobId)) {
            throw new RuntimeException("파일이 삭제되지 않았습니다.");
        }
    }

    // 고유한 파일 이름 생성 (UUID 기반)
    private String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID() + extension; // UUID 기반 고유 이름 생성
    }
}