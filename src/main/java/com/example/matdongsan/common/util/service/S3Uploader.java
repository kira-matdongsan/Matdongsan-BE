package com.example.matdongsan.common.util.service;

import com.example.matdongsan.common.util.domain.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Uploader {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public String upload(FileType fileType, MultipartFile file) throws IOException {
        String key = generateS3Key(fileType, Objects.requireNonNull(file.getOriginalFilename()));

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return generateAccessUrlForKey(key);
    }

    public List<String> uploadAll(FileType fileType, List<MultipartFile> files) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = upload(fileType, file);
            uploadedUrls.add(url);
        }
        return uploadedUrls;
    }

    private String generateS3Key(FileType fileType, String originalName) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String uuid = UUID.randomUUID().toString();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        return String.format("%s/%s/uploads/%s/%s%s",
                fileType.getVisibility().getType(),
                fileType.getDirName(),
                today,
                uuid,
                ext
        );
    }

    private String generateAccessUrlForKey(String key) {
        return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, key);
    }
}
