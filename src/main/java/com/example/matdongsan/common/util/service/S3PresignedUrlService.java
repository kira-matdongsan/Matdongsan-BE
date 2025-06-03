package com.example.matdongsan.common.util.service;

import com.example.matdongsan.common.util.controller.dto.PresignedUrlResponseDto;
import com.example.matdongsan.common.util.domain.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public PresignedUrlResponseDto getPresignedUrl(FileType fileType, String fileName) {
        String key = generateS3Key(fileType, fileName);
        String presignedUrl = generatePresignedUrlForKey(key);
        String accessUrl = generateAccessUrlForKey(key);
        return PresignedUrlResponseDto.builder()
                .fileName(fileName)
                .presignedUrl(presignedUrl)
                .accessUrl(accessUrl)
                .build();
    }

    public List<PresignedUrlResponseDto> getPresignedUrls(FileType fileType, List<String> fileNames) {
        return fileNames.stream()
                .map(name -> {
                    String key = generateS3Key(fileType, name);
                    String presignedUrl = generatePresignedUrlForKey(key);
                    String accessUrl = generateAccessUrlForKey(key);
                    return PresignedUrlResponseDto.builder()
                            .fileName(name)
                            .presignedUrl(presignedUrl)
                            .accessUrl(accessUrl)
                            .build();
                })
                .toList();
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

    private String generatePresignedUrlForKey(String key) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(s3Client.serviceClientConfiguration().region())
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(r -> r
                    .signatureDuration(Duration.ofMinutes(5))
                    .putObjectRequest(putObjectRequest));

            return presignedRequest.url().toString();
        }
    }

    private String generateAccessUrlForKey(String key) {
        return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, key);
    }
}
