package com.bookStore.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

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
                        "YCAJEwuazz93vJ9GnGxqZV7IH",
                        "YCPE1PciWiI-GHYcUuVtmK3bRLEusS6ZF_aw4k87")))
                .build();
    }
    public void uploadPhoto(InputStream stream, String filename, String contentType) throws IOException,
    AmazonServiceException, SdkClientException {

        var file_name = generateUniqueName() + "."+ filename.split("\\.")[1];
        var metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(stream.available());
        s3Client.putObject("lib-backet", file_name, stream, metadata);
    }

    public byte[] downloadPhoto(String filename) throws IOException {
        var s3Object = s3Client.getObject("lib-backet", filename);
        var stream = s3Object.getObjectContent();
        var content = IOUtils.toByteArray(stream);
        s3Object.close();
        return content;
    }

    public String getImageLink(String filename) throws IOException {
        var fileLink = s3Client.getUrl("lib-backet", filename).toExternalForm();
        return  fileLink;
    }

    private String generateUniqueName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
