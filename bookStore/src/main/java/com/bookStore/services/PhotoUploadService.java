package com.bookStore.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.MultipartUpload;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class PhotoUploadService {

    private final AmazonS3 s3Client;
    public PhotoUploadService(){
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                                "https://storage.yandexcloud.net",
                                "ru-central1"
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        "",
                        "")))
                .build();
    }

    public String uploadPhoto (InputStream stream, String filename, String contentType) throws IOException,
    AmazonServiceException, SdkClientException {
        var file_name = generateUniqueName() + "."+ filename.split("\\.")[1];
        var metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(stream.available());
        s3Client.putObject("lib-backet", file_name, stream, metadata);
        return file_name;
    }

    public byte[] downloadPhoto(String filename) throws IOException {
        var s3Object = s3Client.getObject("lib-backet", filename);
        var stream = s3Object.getObjectContent();
        var content = IOUtils.toByteArray(stream);
        s3Object.close();
        return content;
    }

    public String getImageLink(String filename)  {
        String filelink="https://storage.yandexcloud.net/lib-backet/" + filename;
        return filelink;
    }

    private String generateUniqueName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String getFileName(MultipartFile file){
        var fileName = file.getOriginalFilename();
        var fileType = file.getContentType();
        String file_name = null;
        try {
            file_name = uploadPhoto(file.getInputStream(), fileName, fileType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file_name;
    }
}
