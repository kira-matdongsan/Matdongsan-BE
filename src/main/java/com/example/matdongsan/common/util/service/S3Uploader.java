package com.example.matdongsan.common.util.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Uploader {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;

    public String upload(MultipartFile file, String dirName) throws IOException {
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String fileName = dirName + "/" + uuid + ext;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
//                .acl("public-read")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return getUrl(fileName);
    }

    public List<String> uploadAll(List<MultipartFile> files, String dirName) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = upload(file, dirName);
            uploadedUrls.add(url);
        }
        return uploadedUrls;
    }


    public String getUrl(String key) {
        return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, key);
    }
}
